package com.yiming.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.poi.hslf.usermodel.HSLFSlide;
import org.apache.poi.hslf.usermodel.HSLFSlideShow;
import org.apache.poi.hslf.usermodel.HSLFTextParagraph;
import org.apache.poi.hslf.usermodel.HSLFTextRun;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFSlide;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.drawingml.x2006.main.CTRegularTextRun;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextBody;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextCharacterProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextFont;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTextParagraph;
import org.openxmlformats.schemas.presentationml.x2006.main.CTGroupShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTShape;
import org.openxmlformats.schemas.presentationml.x2006.main.CTSlide;

public class PPT2ImageUtil {
    /**
     * 将PPTX 文件转换成image
     * 
     * @param orignalPPTFileName
     *            //PPTX文件路径 如：d:/demo/demo1.pptx
     * @param targetImageFileDir
     *            //转换后的图片保存路径 如：d:/demo/pptxImg
     * @param imageFormatNameString
     *            //图片转化的格式字符串 ，如："jpg"、"jpeg"、"bmp" "png" "gif" "tiff"
     * @return Map<String,Object> key: converReturnResult 类型：boolean 转化结果 true
     *         代表转换成功，false 代表转换失败 key:imgNames 类型：List<String> 转换成功后图片的全部名称集合
     *         注：获取“imgNames”图片名称集合时，请先判断“converReturnResult”
     *         是否为true；如果有一张转换失败则为false
     */
    @SuppressWarnings("resource")
    public static Map<String, Object> converPPTXtoImage(String orignalPPTFileName, String targetImageFileDir,
            String imageFormatNameString) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean converReturnResult = false;// 是否全部转成功
        List<String> imgNamesList = new ArrayList<String>();// PPT转成图片后所有名称集合
        FileInputStream orignalPPTFileInputStream = null;
        FileOutputStream orignalPPTFileOutStream = null;
        XMLSlideShow oneSlideShow = null;
        try {
            try {
                orignalPPTFileInputStream = new FileInputStream(orignalPPTFileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                map.put("converReturnResult", converReturnResult);
                return map;
            }

            try {
                oneSlideShow = new XMLSlideShow(orignalPPTFileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                map.put("converReturnResult", converReturnResult);
                return map;
            }
            // 获取PPT每页的尺寸大小（宽和高度）
            Dimension onePPTPageSize = oneSlideShow.getPageSize();
            // 获取PPT文件中的所有PPT页面，并转换为一张张播放片
            List<XSLFSlide> pptPageXSLFSLiseList = oneSlideShow.getSlides();

            String xmlFontFormat = "<xml-fragment xmlns:a=\"http://schemas.openxmlformats.org/drawingml/2006/main\" xmlns:r=\"http://schemas.openxmlformats.org/officeDocument/2006/relationships\" xmlns:p=\"http://schemas.openxmlformats.org/presentationml/2006/main\">"
                    + "<a:rPr lang=\"zh-CN\" altLang=\"en-US\" dirty=\"0\" smtClean=\"0\"> "
                    + "<a:latin typeface=\"+mj-ea\"/> " + "</a:rPr>" + "</xml-fragment>";

            for (int i = 0; i < pptPageXSLFSLiseList.size(); i++) {
                /**
                 * 设置中文为宋体，解决中文乱码问题
                 */
                CTSlide oneCTSlide = pptPageXSLFSLiseList.get(i).getXmlObject();
                CTGroupShape oneCTGroupShape = oneCTSlide.getCSld().getSpTree();
                List<CTShape> oneCTShapeList = oneCTGroupShape.getSpList();
                for (CTShape ctShape : oneCTShapeList) {
                    CTTextBody oneCTTextBody = ctShape.getTxBody();

                    if (null == oneCTTextBody) {
                        continue;
                    }
                    CTTextParagraph[] oneCTTextParagraph = oneCTTextBody.getPArray();
                    CTTextFont oneCTTextFont = null;
                    try {
                        oneCTTextFont = CTTextFont.Factory.parse(xmlFontFormat);
                    } catch (XmlException e) {
                        e.printStackTrace();
                    }

                    for (CTTextParagraph ctTextParagraph : oneCTTextParagraph) {
                        CTRegularTextRun[] onrCTRegularTextRunArray = ctTextParagraph.getRArray();
                        for (CTRegularTextRun ctRegularTextRun : onrCTRegularTextRunArray) {
                            CTTextCharacterProperties oneCTTextCharacterProperties = ctRegularTextRun.getRPr();
                            oneCTTextCharacterProperties.setLatin(oneCTTextFont);

                        }

                    }
                }

                // 创建BufferedImage 对象，图像尺寸为原来的PPT的每页尺寸

                BufferedImage oneBufferedImage = new BufferedImage(onePPTPageSize.width, onePPTPageSize.height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D oneGraphics2D = oneBufferedImage.createGraphics();
                // 将PPT文件中的每个页面中的相关内容画到转换后的图片中
                pptPageXSLFSLiseList.get(i).draw(oneGraphics2D);
                /**
                 * 设置图片的存放路径和图片格式，注意生成的文件路径为绝对路径，最终获得各个图像文件所对应的输出流的对象
                 */

                try {
                    String imgName = (i + 1) + "_" + UUID.randomUUID().toString() + "." + imageFormatNameString;
                    imgNamesList.add(imgName);// 将图片名称添加的集合中

                    orignalPPTFileOutStream = new FileOutputStream(targetImageFileDir + imgName);
                    // orignalPPTFileOutStream=new
                    // FileOutputStream(targetImageFileDir+(i+1)+"_"+UUID.randomUUID().toString()+"."+imageFormatNameString);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    map.put("converReturnResult", converReturnResult);
                    return map;
                }

                // 将转换后的各个图片文件保存带指定的目录中
                try {
                    ImageIO.write(oneBufferedImage, imageFormatNameString, orignalPPTFileOutStream);
                } catch (IOException e) {
                    e.printStackTrace();
                    map.put("converReturnResult", converReturnResult);
                    return map;
                }

            }

        } finally {
            try {
                if (orignalPPTFileInputStream != null) {
                    orignalPPTFileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (orignalPPTFileOutStream != null) {
                    orignalPPTFileOutStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            converReturnResult = true;
            map.put("converReturnResult", converReturnResult);
            map.put("imgNames", imgNamesList);
        }

        return map;
    }

    /**
     * 将PPT 文件转换成image
     * 
     * @param orignalPPTFileName
     *            //PPT文件路径 如：d:/demo/demo1.ppt
     * @param targetImageFileDir
     *            //转换后的图片保存路径 如：d:/demo/pptImg
     * @param imageFormatNameString
     *            //图片转化的格式字符串 ，如："jpg"、"jpeg"、"bmp" "png" "gif" "tiff"
     * @return Map<String,Object> key: converReturnResult 类型：boolean 转化结果 true
     *         代表转换成功，false 代表转换失败 key:imgNames 类型：List<String> 转换成功后图片的全部名称集合
     *         注：获取“imgNames”图片名称集合时，请先判断“converReturnResult”
     *         是否为true；如果有一张转换失败则为false
     */
    @SuppressWarnings("resource")
    public static Map<String, Object> converPPTtoImage(String orignalPPTFileName, String targetImageFileDir,
            String imageFormatNameString) {
        Map<String, Object> map = new HashMap<String, Object>();
        boolean converReturnResult = false;// 是否全部转成功
        List<String> imgNamesList = new ArrayList<String>();// PPT转成图片后所有名称集合

        FileInputStream orignalPPTFileInputStream = null;
        FileOutputStream orignalPPTFileOutStream = null;
        HSLFSlideShow oneHSLFSlideShow = null;

        try {
            try {
                orignalPPTFileInputStream = new FileInputStream(orignalPPTFileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                map.put("converReturnResult", converReturnResult);
                return map;
            }

            try {
                oneHSLFSlideShow = new HSLFSlideShow(orignalPPTFileInputStream);
            } catch (IOException e) {
                e.printStackTrace();
                map.put("converReturnResult", converReturnResult);
                return map;
            }
            // 获取PPT每页的大小（宽和高度）
            Dimension onePPTPageSize = oneHSLFSlideShow.getPageSize();

            // 获得PPT文件中的所有的PPT页面（获得每一张幻灯片）,并转为一张张的播放片
            List<HSLFSlide> pptPageSlideList = oneHSLFSlideShow.getSlides();
            // 下面循环的主要功能是实现对PPT文件中的每一张幻灯片进行转换和操作
            for (int i = 0; i < pptPageSlideList.size(); i++) {
                // 这几个循环只要是设置字体为宋体，防止中文乱码，
                List<List<HSLFTextParagraph>> oneTextParagraphs = pptPageSlideList.get(i).getTextParagraphs();
                for (List<HSLFTextParagraph> list : oneTextParagraphs) {
                    for (HSLFTextParagraph hslfTextParagraph : list) {
                        List<HSLFTextRun> HSLFTextRunList = hslfTextParagraph.getTextRuns();
                        for (int j = 0; j < HSLFTextRunList.size(); j++) {

                            /*
                             * 如果PPT在WPS中保存过，则 HSLFTextRunList.get(j).getFontSize();的值为0或者26040，
                             * 因此首先识别当前文本框内的字体尺寸是否为0或者大于26040，则设置默认的字体尺寸。
                             * 
                             */
                            // 设置字体大小
                            Double size = HSLFTextRunList.get(j).getFontSize();
                            if ((size <= 0) || (size >= 26040)) {
                                HSLFTextRunList.get(j).setFontSize(20.0);
                            }
                            // 设置字体样式为宋体
                            // String family=HSLFTextRunList.get(j).getFontFamily();
                            HSLFTextRunList.get(j).setFontFamily("宋体");

                        }
                    }

                }
                /**
                 * 创建BufferedImage对象，图像的尺寸为原来的每页的尺寸
                 */
                BufferedImage oneBufferedImage = new BufferedImage(onePPTPageSize.width, onePPTPageSize.height,
                        BufferedImage.TYPE_INT_RGB);
                Graphics2D oneGraphics2D = oneBufferedImage.createGraphics();
                /**
                 * 设置转换后的图片背景色为白色
                 * 
                 */
                oneGraphics2D.setPaint(Color.white);
                oneGraphics2D.fill(new Rectangle2D.Float(0, 0, onePPTPageSize.width, onePPTPageSize.height));
                pptPageSlideList.get(i).draw(oneGraphics2D);
                /**
                 * 设置图片的存放路径和图片格式，注意生成的图片路径为绝对路径，最终获得各个图像文件所对应的输出流对象
                 */
                try {

                    String imgName = (i + 1) + "_" + UUID.randomUUID().toString() + "." + imageFormatNameString;
                    imgNamesList.add(imgName);// 将图片名称添加的集合中

                    orignalPPTFileOutStream = new FileOutputStream(targetImageFileDir + imgName);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    map.put("converReturnResult", converReturnResult);
                    return map;
                }
                /**
                 * 转换后的图片文件保存的指定的目录中
                 */

                try {
                    ImageIO.write(oneBufferedImage, imageFormatNameString, orignalPPTFileOutStream);
                    // throw new IOException();
                } catch (IOException e) {
                    e.printStackTrace();
                    map.put("converReturnResult", converReturnResult);
                    return map;
                }

            }

        } finally {
            try {
                if (orignalPPTFileInputStream != null) {
                    orignalPPTFileInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if (orignalPPTFileOutStream != null) {
                    orignalPPTFileOutStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            converReturnResult = true;
            map.put("converReturnResult", converReturnResult);
            map.put("imgNames", imgNamesList);
        }

        return map;
    }
    
    public static String getPPTImage(String imgPath, String ctxPath, String realFileName, String fileURL) {
        String imgURLs = "";
        File imgFolder = new File(imgPath);
        if (!imgFolder.exists()) {
            imgFolder.mkdir();
        }
        Map<String, Object> map = converPPTtoImage(ctxPath + realFileName, imgPath, "jpg");
        boolean converReturnResult = (Boolean) map.get("converReturnResult");
        if (converReturnResult) { // 如果全部转换成功,则为true;如果有一张转换失败,则为fasle
            @SuppressWarnings("unchecked")
            List<String> imgNames = (List<String>) map.get("imgNames");
            for (String imgName : imgNames) {
                String imgURL = fileURL + imgName;
                imgURLs += imgURL + ";";
            }
            imgURLs = imgURLs.substring(0, imgURLs.length() - 1);
        }
        return imgURLs;
    }
    
    public static String getPPTXImage(String imgPath, String ctxPath, String realFileName, String fileURL) {
        String imgURLs = "";
        File imgFolder = new File(imgPath);
        if (!imgFolder.exists()) {
            imgFolder.mkdir();
        }
        Map<String, Object> map = converPPTXtoImage(ctxPath + realFileName, imgPath, "jpg");
        boolean converReturnResult = (Boolean) map.get("converReturnResult");
        if (converReturnResult) { // 如果全部转换成功,则为true;如果有一张转换失败,则为fasle
            @SuppressWarnings("unchecked")
            List<String> imgNames = (List<String>) map.get("imgNames");
            for (String imgName : imgNames) {
                String imgURL = fileURL + imgName;
                imgURLs += imgURL + ";";
            }
            imgURLs = imgURLs.substring(0, imgURLs.length() - 1);
        }
        return imgURLs;
    }
}
