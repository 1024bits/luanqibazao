package com.aisino.vip.common.util;

import com.lowagie.text.pdf.BaseFont;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.util.Date;
import java.util.Map;

/**
 * Created by fangfan
 * on 2019/5/29
 */
public class Html2PDFUtil {

    /*<dependency>
            <!-- jsoup HTML parser library @ https://jsoup.org/ -->
            <groupId>org.jsoup</groupId>
            <artifactId>jsoup</artifactId>
            <version>1.11.2</version>
        </dependency>

        <!-- 生成PDF相关 start-->
        <dependency>
            <groupId>org.xhtmlrenderer</groupId>
            <artifactId>flying-saucer-pdf-openpdf</artifactId>
            <version>9.1.18</version>
        </dependency>
    */
    public static byte[] contextLoads(Map<String,Object> content) {
        if(content==null || content.size()<1){
            return null;
        }
//        Date start = new Date();
//        System.out.println("开始生成时间"+ start.getTime());
        // agr_title 协议名称
        String title = content.get("agrTitle")==null? "":content.get("agrTitle").toString();
        //`seller_account` '销方开户银行和账号',
        String sellerAccount = content.get("sellerAccount")==null? "":content.get("sellerAccount").toString();
        //packageName,  商品标题
        String packageName = content.get("packageName")==null? "":content.get("packageName").toString();
        //buyerName,  购买者公司名称
        String buyerName = content.get("buyerName")==null? "":content.get("buyerName").toString();
        //buyerTaxnum, 购买者所属企业税号
        String buyerTaxnum = content.get("buyerTaxnum")==null? "":content.get("buyerTaxnum").toString();
        //agrContent,  协议内容
        String agrContent = content.get("agrContent")==null? "":content.get("agrContent").toString();
        //sellerName,  销方名称
        String sellerName = content.get("sellerName")==null? "":content.get("sellerName").toString();
        //sellerTaxnum, 销方税号
        String sellerTaxnum = content.get("sellerTaxnum")==null? "":content.get("sellerTaxnum").toString();
        //sellerAddr, 销方电话地址
        String sellerAddr = content.get("sellerAddr")==null? "":content.get("sellerAddr").toString();
        // 销方电话
//        String sellerPhone = content.get("sellerPhone")==null? "":content.get("sellerPhone").toString();
//        if(StringUtils.isNotBlank(sellerPhone)){
//            sellerAddr = sellerPhone;
//        }
        // orderId , 订单编号
        String orderId = content.get("orderId").toString();
        agrContent = JsoupFilter.filterUserInputContent(agrContent);
        String unStrictContent = "<div class=\"m-protocol\"><div class=\"bold-title m-protocol-title\"><center><b>" +
                title +
                "</b></center></div><div class=\"m-info-character f-clearfix e-mt20\">" +
                "<div class=\"m-buyer f-fl\"><p class=\"m-info-title\">" +
                "甲方(购方)</p><p>企业名称：" +buyerName+
                "</p><p>企业税号："+buyerTaxnum+"</p><p>套餐名称："+packageName+"</p><p>订单编号："+orderId+"</p>" +
                "</div><div class=\"m-seller f-fl\"><p class=\"m-info-title\">乙方(销方)</p><p>企业名称："+sellerName+"</p>" +
                "<p>企业税号："+sellerTaxnum+"</p><p>地址电话："+sellerAddr+"</p><p>开户银行和账号："+sellerAccount+"</p></div></div>" +
                agrContent;
//                "<div class=\"m-content-protocol e-mt20\"><img src=\"http://192.168.210.110/group1/M00/04/5F/wKjScVzAGtSAHD_gAAS_o9Cx7Po291.jpg\" alt=\"\">\n" +
//                "<p style=\"color:#444444;font-size:16px;\">\n" +
//                "\t当地时间4月22日，《复仇者联盟4：终局之战》在洛杉矶举行全球首映礼。\n" +
//                "</p>\n" +
//                "<p style=\"color:#444444;font-size:16px;\">\n" +
//                "\t先来看一波帅哥美女！<br>“星爵”和“黑寡妇”\n" +
//                "</p></div></div>";
//        System.out.println(Jsoup.parse(unStrictContent).html());
        Document doc2 = Jsoup.parse(unStrictContent);
        doc2.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);
//        System.out.println(doc2.body().html());
        try {
            StringBuilder stringBuilder = new StringBuilder().append(
                    "<!DOCTYPE html\n" +
                            "    PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
                            "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                            "\n" +
                            "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">")
                    .append("</html>");
            Document template = Jsoup.parse(stringBuilder.toString());
            template.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);
            template.head().append("<style> body { margin-left: 45px; margin-right: 45px; font-family: HYKaiTiJ;}</style>");
            template.body().append(doc2.body().html());
//            System.out.println(template.html());
//            createPDF(template.html(),"D:\\srv\\20190820.pdf");
            byte[] result = createPDF(template.html());
//            Date end = new Date();
//            System.out.println(end.getTime()-start.getTime());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] contextLoads2Loacl(Map<String,Object> content) {
        if(content==null || content.size()<1){
            return null;
        }
        Date start = new Date();
        System.out.println("开始生成时间"+ start.getTime());
        // agr_title 协议名称
        String title = content.get("agrTitle")==null? "":content.get("agrTitle").toString();
        //`seller_account` '销方开户银行和账号',
        String sellerAccount = content.get("sellerAccount")==null? "":content.get("sellerAccount").toString();
        //packageName,  商品标题
        String packageName = content.get("packageName")==null? "":content.get("packageName").toString();
        //buyerName,  购买者公司名称
        String buyerName = content.get("buyerName")==null? "":content.get("buyerName").toString();
        //buyerTaxnum, 购买者所属企业税号
        String buyerTaxnum = content.get("buyerTaxnum")==null? "":content.get("buyerTaxnum").toString();
        //agrContent,  协议内容
        String agrContent = content.get("agrContent")==null? "":content.get("agrContent").toString();
        //sellerName,  销方名称
        String sellerName = content.get("sellerName")==null? "":content.get("sellerName").toString();
        //sellerTaxnum, 销方税号
        String sellerTaxnum = content.get("sellerTaxnum")==null? "":content.get("sellerTaxnum").toString();
        //sellerAddr, 销方电话地址
        String sellerAddr = content.get("sellerAddr")==null? "":content.get("sellerAddr").toString();
        // 销方电话
//        String sellerPhone = content.get("sellerPhone")==null? "":content.get("sellerPhone").toString();
//        if(StringUtils.isNotBlank(sellerPhone)){
//            sellerAddr = sellerPhone;
//        }
        agrContent = JsoupFilter.filterUserInputContent(agrContent);
        System.out.println(agrContent);
        // orderId , 订单编号
        String orderId = content.get("orderId").toString();
        String unStrictContent = "<div class=\"m-protocol\"><div class=\"bold-title m-protocol-title\"><center><b>" +
                title +
                "</b></center></div><div class=\"m-info-character f-clearfix e-mt20\">" +
                "<div class=\"m-buyer f-fl\"><p class=\"m-info-title\">" +
                "甲方(购方)</p><p>企业名称：" +buyerName+
                "</p><p>企业税号："+buyerTaxnum+"</p><p>套餐名称："+packageName+"</p><p>订单编号："+orderId+"</p>" +
                "</div><div class=\"m-seller f-fl\"><p class=\"m-info-title\">乙方(销方)</p><p>企业名称："+sellerName+"</p>" +
                "<p>企业税号："+sellerTaxnum+"</p><p>地址电话："+sellerAddr+"</p><p>开户银行和账号："+sellerAccount+"</p></div></div>" +
                agrContent;
//                "<div class=\"m-content-protocol e-mt20\"><img src=\"http://192.168.210.110/group1/M00/04/5F/wKjScVzAGtSAHD_gAAS_o9Cx7Po291.jpg\" alt=\"\">\n" +
//                "<p style=\"color:#444444;font-size:16px;\">\n" +
//                "\t当地时间4月22日，《复仇者联盟4：终局之战》在洛杉矶举行全球首映礼。\n" +
//                "</p>\n" +
//                "<p style=\"color:#444444;font-size:16px;\">\n" +
//                "\t先来看一波帅哥美女！<br>“星爵”和“黑寡妇”\n" +
//                "</p></div></div>";
//        System.out.println(Jsoup.parse(unStrictContent).html());
        Document doc2 = Jsoup.parse(unStrictContent);
        doc2.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);
//        System.out.println(doc2.body().html());
        try {
            StringBuilder stringBuilder = new StringBuilder().append(
                    "<!DOCTYPE html\n" +
                            "    PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"\n" +
                            "    \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n" +
                            "\n" +
                            "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\" lang=\"en\">")
                    .append("</html>");
            Document template = Jsoup.parse(stringBuilder.toString());
            template.outputSettings().syntax(Document.OutputSettings.Syntax.xml).escapeMode(Entities.EscapeMode.xhtml);
            template.head().append("<style> body { margin-left: 45px; margin-right: 45px; font-family: HYKaiTiJ;}</style>");
            template.body().append(doc2.body().html());
//            System.out.println(template.html());
            String name = UUIDUtil.lowerCaseString();
            createPDF(template.html(),"D:\\srv\\"+name+".pdf");
//            byte[] result = createPDF(template.html());
            Date end = new Date();
            System.out.println(end.getTime()-start.getTime());
//            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static byte[] createPDF(String content) throws IOException {
//        OutputStream os = null;
        InputStream in = null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try {
            ITextRenderer render = new ITextRenderer();
            render.getFontResolver().addFont("/HYKaiTiJ.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            render.setDocumentFromString(content);
            render.layout();
            render.createPDF(os);
            render.finishPDF();
//            os.flush();
            in = new ByteArrayInputStream(os.toByteArray());
            byte[] result = IOUtils.toByteArray(in);
            os.close();
            os = null;
            return result;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }



    // 输出流到本地
    private static void createPDF(String content, String file) throws IOException, com.lowagie.text.DocumentException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            ITextRenderer render = new ITextRenderer();
            render.getFontResolver().addFont("/HYKaiTiJ.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            render.setDocumentFromString(content);
            render.layout();
            render.createPDF(os);
            render.finishPDF();

            os.flush();
            os.close();
            os = null;
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }

//    public static void main(String[] args) {
//        Html2PDFUtil util = new Html2PDFUtil();
//        util.contextLoads("3213");
//    }
}
