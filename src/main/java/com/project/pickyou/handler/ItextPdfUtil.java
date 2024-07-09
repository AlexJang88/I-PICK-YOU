package com.project.pickyou.handler;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.project.pickyou.dto.ContractDTO;
import com.project.pickyou.dto.ItextPdfDto;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class ItextPdfUtil {
    /*
     * PDF 유무를 체크한 후
     * PDF 파일이 없을 경우 PDF 파일 생성 메소드 실행
     */
    public File checkPDF (ItextPdfDto pdfDto) {
        File file = new File(pdfDto.getPdfFilePath(),pdfDto.getPdfFileName());
        int fileSize = (int) file.length();
        if (fileSize == 0) {
            createPDF(pdfDto);
            file = new File(pdfDto.getPdfFilePath(),pdfDto.getPdfFileName());
        }
        return file;
    }

    /*
     * iText 라이브러리를 사용한 PDF 파일 생성
     * CSS , Font 설정 기능 포함
     * */
    public void createPDF(ItextPdfDto itextPdfDto) {

        // 최초 문서 사이즈 설정
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);

        try {
            // PDF 파일 생성
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(itextPdfDto.getPdfFilePath()+itextPdfDto.getPdfFileName()));
            // PDF 파일에 사용할 폰트 크기 설정
            pdfWriter.setInitialLeading(12.5f);
            // PDF 파일 열기
            document.open();

            // XMLWorkerHelper xmlWorkerHelper = XMLWorkerHelper.getInstance();

            // CSS 설정 변수 세팅
            CSSResolver cssResolver = new StyleAttrCSSResolver();
            CssFile cssFile = null;

            try {
                /*
                 * CSS 파일 설정
                 * 기존 방식은 FileInputStream을 사용했으나, jar 파일로 빌드 시 파일을 찾을 수 없는 문제가 발생
                 * 따라서, ClassLoader를 사용하여 파일을 읽어오는 방식으로 변경
                 */
                InputStream cssStream = getClass().getClassLoader().getResourceAsStream("static/css/ItextPdf.css");

                // CSS 파일 담기
                cssFile = XMLWorkerHelper.getCSS(cssStream);
//                cssFile = XMLWorkerHelper.getCSS(new FileInputStream("src/main/resources/static/css/test.css"));
            } catch (Exception e) {
                throw new IllegalArgumentException("PDF CSS 파일을 찾을 수 없습니다.");
            }

            if(cssFile == null) {
                throw new IllegalArgumentException("PDF CSS 파일을 찾을 수 없습니다.");
            }

            // CSS 파일 적용
            cssResolver.addCss(cssFile);

            // PDF 파일에 HTML 내용 삽입
            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);

            /*
             * 폰트 설정
             * CSS 와 다르게, fontProvider.register() 메소드를 사용하여 폰트를 등록해야 함
             * 해당 메소드 내부에서 경로처리를 하여 개발, 배포 시 폰트 파일을 찾을 수 있도록 함
             * */
            try {
                fontProvider.register("static/font/AppleSDGothicNeoR.ttf", "AppleSDGothicNeo");
            } catch (Exception e) {
                throw new IllegalArgumentException("PDF 폰트 파일을 찾을 수 없습니다.");
            }

            if(fontProvider.getRegisteredFonts() == null) {
                throw new IllegalArgumentException("PDF 폰트 파일을 찾을 수 없습니다.");
            }

            // 사용할 폰트를 담아두었던 내용을
            // CSSAppliersImpl에 담아 적용
            CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);

            // HTML Pipeline 생성
            HtmlPipelineContext htmlPipelineContext = new HtmlPipelineContext(cssAppliers);
            htmlPipelineContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

            // ========================================================================================
            // Pipelines
            PdfWriterPipeline pdfWriterPipeline = new PdfWriterPipeline(document, pdfWriter);
            HtmlPipeline htmlPipeline = new HtmlPipeline(htmlPipelineContext, pdfWriterPipeline);
            CssResolverPipeline cssResolverPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
            // ========================================================================================


            // ========================================================================================
            // XMLWorker
            XMLWorker xmlWorker = new XMLWorker(cssResolverPipeline, true);
            XMLParser xmlParser = new XMLParser(true, xmlWorker, StandardCharsets.UTF_8);
            // ========================================================================================


            /* HTML 내용을 담은 String 변수
            주의점
            1. HTML 태그는 반드시 닫아야 함
            2. xml 기준 html 태그 확인( ex : <p> </p> , <img/> , <col/> )
            위 조건을 지키지 않을 경우 DocumentException 발생
            */
            String htmlStr = getHtml(itextPdfDto.getContractDTO());

            // HTML 내용을 PDF 파일에 삽입
            StringReader stringReader = new StringReader(htmlStr);
            // XML 파싱
            xmlParser.parse(stringReader);
            // PDF 문서 닫기
            document.close();
            // PDF Writer 닫기
            pdfWriter.close();

        } catch (DocumentException e1) {
            throw new IllegalArgumentException("PDF 라이브러리 설정 에러");
        } catch (FileNotFoundException e2) {
            e2.printStackTrace();
            throw new IllegalArgumentException("PDF 파일 생성중 에러");
        } catch (IOException e3) {
            e3.printStackTrace();
            throw new IllegalArgumentException("PDF 파일 생성중 에러2");
        } catch (Exception e4) {
            e4.printStackTrace();
            throw new IllegalArgumentException("PDF 파일 생성중 에러3");
        }
        finally {
            try {
                document.close();
            } catch (Exception e) {
                System.out.println("PDF 파일 닫기 에러");
                e.printStackTrace();
            }
        }

    }

    // 사용할 html 코드를 가져오는 메소드
    public String getHtml(ContractDTO dto) {

        String return_html = "";
       return_html = "<html>" +
                        "<body>" +


//               "<div class='content'>"+
//                        "<div class='title'>건설일용근로자 표준근로계약서</div>"+
//                       "<div class='content'>"+
//                    "<p> <span th:text='${company.companyInfo.name}'></span> (이하 '사업주'라 함)과(와) <span th:text='${member.memberInfo.name}'></span> (이하 '근로자'라 함)는 다음과 같이 근로계약을 체결한다.</p>"+
//                        "<table>"+
//                                "<tr>"+
//                                    "<th>1. 근로계약기간</th>"+
//                                    "<td>"+
//                                        "<span th:text='${#temporals.format(contract.startDate, 'yyyy')}'></span>년"+
//                <span th:text="${#temporals.format(contract.startDate, 'MM')}"></span>월
//                <span th:text="${#temporals.format(contract.startDate, 'dd')}"></span>일
//        부터
//                <span th:text="${#temporals.format(contract.endDate, 'yyyy')}"></span>년
//                <span th:text="${#temporals.format(contract.endDate, 'MM')}"></span>월
//                <span th:text="${#temporals.format(contract.endDate, 'dd')}"></span>일 까지
//                <br> ※ 근로계약기간을 정하지 않는 경우에는 "근로개시일"만 기재
//                                    </td>
//                                </tr>
//                                <tr>
//                                    <th>2. 근 무 장 소</th>
//                                    <td><span th:text="${contract.location}"></span></td>
//                                </tr>
//                                <tr>
//                                    <th>3. 업무의 내용(직종)</th>
//                                    <td><span th:text="${contract.job}"></span></td>
//                                </tr>
//                                <tr>
//                                    <th>4. 소정근로시간</th>
//                                    <td><span th:text="${#strings.substring(contract.workStartTime,0,5)}"></span>부터 <span th:text="${#strings.substring(contract.workStartTime,0,5)}"></span>까지 </td>
//                                </tr>
//                                <tr>
//                                    <th>5. 근무일/휴일</th>
//                                    <td>매주 <span th:text="${contract.worksSchedule}"></span>일(또는 매일단위)근무 <br> ※ 주휴일은 1주간 소정근로일을 모두 근로한 경우에 주당 1일을 유급으로 부여</td>
//                                </tr>
//                                <tr>
//                                    <th>6. 임 금</th>
//                                    <td>
//                                        <p>- <span th:text="${contract.wageType}"></span>금: <span th:text="${contract.wage}"></span>원</p>
//                                        <p>- 기타 제수당(시간외근로수당, 야간·휴일근로수당 등): <span th:text="${contract.etc}"></span></p>
//                                        <p>- 임금지급일: <span th:text="${contract.payDate}"> </span><span th:text="${contract.customPayDate}">일(휴일의 경우는 전일 지급)</span></p>
//                                        <p>- 지급방법: <span th:text="${contract.wageInto}"></span></p>
//                                    </td>
//                                </tr>
//                                <tr>
//                                    <th>7. 연차유급휴가</th>
//                                    <td>연차유급휴가는 근로기준법에서 정하는 바에 따라 부여함</td>
//                                </tr>
//                                <tr>
//                                    <th>8. 사회보험 적용여부</th>
//                                    <td>
//                                        <p>□ 고용보험(<span th:if="${contract.insurance1=='없음'}" >해당없음</span><span th:unless="${contract.insurance1=='없음'}" >해당</span>)
//                                            □ 산재보험(<span th:if="${contract.insurance2=='없음'}" >해당없음</span><span th:unless="${contract.insurance2=='없음'}" >해당</span>)
//                                            □ 국민연금(<span th:if="${contract.insurance3=='없음'}" >해당없음</span><span th:unless="${contract.insurance3=='없음'}" >해당</span>)
//                                            □ 건강보험(<span th:if="${contract.insurance4=='없음'}" >해당없음</span><span th:unless="${contract.insurance4=='없음'}" >해당</span>)</p>
//                                    </td>
//                                </tr>
//                                <tr>
//                                    <th>9. 근로계약서 교부</th>
//                                    <td>사업주는 근로계약을 체결함과 동시에 본 계약서를 사본하여 "근로자"의 교부요구와 관계없이 "근로자"에게 교부함(근로기준법 제17조 이행)</td>
//                                </tr>
//                                <tr>
//                                    <th>10. 근로계약, 취업규칙 등의 성실한 이행의무</th>
//                                    <td>사업주와 근로자는 각각 근로계약, 취업규칙, 단체협약을 지키고 성실하게 이행하여야 함</td>
//                                </tr>
//                                <tr>
//                                    <th>11. 기 타</th>
//                                    <td>이 계약에 정함이 없는 사항은 근로기준법령에 의함</td>
//                                </tr>
//                            </table>
//                            <br>
//                            <h3><span th:text="${#temporals.format(contract.contractDate, 'yyyy')}"></span>년
//                <span th:text="${#temporals.format(contract.contractDate, 'MM')}"></span>월
//                <span th:text="${#temporals.format(contract.contractDate, 'dd')}"></span>일</h3>
//                            <h4>사업주 </h4>
//                            <h4>사업체 명 : <span th:text="${company.companyInfo.companyName}"> </span> (전화: <span th:text="${company.phone}"> </span>)</h4>
//                            <h4>주소: <span th:text="${company.address}"> </span></h4>
//                            <h4>대표자: <span th:text="${company.companyInfo.name}"> </span><img src="" id="img01"/></h4>
//                            <h4>근로자 </h4>
//                            <h4>연락처 : <span th:text="${member.phone}"> </span></h4>
//                            <h4>주소: <span th:text="${member.address}"> </span></h4>
//                            <h4>성 함: <span th:text="${member.memberInfo.name}"> </span><img src="" id="img02"/></h4>
//                            <input type="hidden" id="signCheck" th:value=${signCheck}>
//                            <input type="hidden" id="comSign" th:value="${comSign}">
//                            <input type="hidden" id="memSign" th:value="${memSign}">
//                            <br>
//                            <input type="hidden" name="contractId" id="contractId" th:value="${contract.id}">
//                            <p style="text-align: center;">
//                                <button id="signButton" >서명하기</button>
//                            </p>
//                            <div class="wrapper">
//                                <canvas id="signature-pad" class="signature-pad" width=400 height=200></canvas>
//                            </div>
//                            <button id="save-png" style="display: none;">저장</button>
//                            <button id="clear" style="display: none;">다시쓰기</button>
//                        </div>
//                    </div>"+
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//                        "<h1>hyeok</h1>" +
//                        "<p>이미지 테스트 합니다.</p>" +
//                        "<img src='http://localhost:8080/images/test.png' />" +
//













               "</body>" +
                        "</html>";
        return return_html;
    }
}
