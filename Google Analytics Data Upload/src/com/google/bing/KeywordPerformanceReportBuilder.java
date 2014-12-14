package com.google.bing;

import java.rmi.RemoteException;

import javax.xml.soap.*;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;

import com.google.analytics.AnalyticsProfile;

public class KeywordPerformanceReportBuilder {
	
	final static String serverURI = "https://bingads.microsoft.com/Reporting/v9";
	final static String url = "https://api.bingads.microsoft.com/Api/Advertiser/Reporting/V9/ReportingService.svc?singleWsdl";
	
	static String bingApiUsername;
	static String bingApiPassword;
	static String bingAccountId;
	static String bingCustomerId;
	static String bingCampaignId;
	static String bingDeveloperToken;

	/**
	 * @return
	 * @param
	 * @throws
	 */
	public String runReport(AnalyticsProfile analytics_profile) throws Exception {
		
		this.bingApiUsername = analytics_profile.getBingApiName();
		this.bingApiPassword = analytics_profile.getBingPassword();
		this.bingAccountId = analytics_profile.getBingAccountId();
		this.bingCustomerId = analytics_profile.getBingCustomerId();
		this.bingCampaignId = "170483295";
		this.bingDeveloperToken = analytics_profile.getBingDeveloperToken();

		SOAPConnection soapConnection = openSoapConnection();
		SOAPMessage soapMessage = openSoapMessage();
		SOAPPart soapPart = openSoapPart(soapMessage);
		SOAPEnvelope envelope = openSoapEnvelope(soapPart);
		envelope = buildSoapHeader(envelope);
		envelope = buildSoapBody(envelope);
		MimeHeaders headers = buildMimeHeader(soapMessage);
		SubmitGenerateReport(headers, soapMessage);
		
		SOAPMessage soapResponse = soapResponse(soapMessage,
				soapConnection);
		String reportID = soapResponse.getSOAPBody().getTextContent();
		
		// System.out.println("Report Request ID: " + reportID + "\n");
		int waitTime = 1000 * 10 * 1;
		SOAPMessage reportRequest = null;

		for (int i = 0; i < 10; i++) {
			try {
				Thread.sleep(waitTime);
			} catch (InterruptedException ignore) {
			}
			reportRequest = PollGenerateReport(reportID);
			String status = getStatus(reportRequest);
			if (status == "Pending" || status == "Status") {
				System.out.println("Report Request status: " + status + "\n");
			} else {
				break;
			}
		}
		String newurl = getUrl(reportRequest);

		return newurl;
	}
	
	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static MimeHeaders buildMimeHeader(SOAPMessage soapMessage) {
		MimeHeaders headers = soapMessage.getMimeHeaders();
		return headers;
	}

	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static SOAPEnvelope buildSoapBody(SOAPEnvelope envelope)
			throws SOAPException {
		SOAPBody soapBody = envelope.getBody();
		soapBody.setPrefix("soapenv");

		SOAPElement SubmitGenerateReportRequest = soapBody.addChildElement(
				"SubmitGenerateReportRequest", "v9");
		SOAPElement ReportRequest = SubmitGenerateReportRequest
				.addChildElement("ReportRequest", "v9");
		ReportRequest.setAttribute("xsi:type",
				"v9:KeywordPerformanceReportRequest");
		SOAPElement Format = ReportRequest.addChildElement("Format", "v9");
		Format.addTextNode("Csv");
		SOAPElement Language = ReportRequest.addChildElement("Language", "v9");
		Language.setAttribute("xsi:nil", "true");
		SOAPElement ReportName = ReportRequest.addChildElement("ReportName",
				"v9");
		ReportName.addTextNode("My Keyword Performance Report");
		SOAPElement ReturnOnlyCompleteData = ReportRequest.addChildElement(
				"ReturnOnlyCompleteData", "v9");
		ReturnOnlyCompleteData.addTextNode("false");
		SOAPElement Aggregation = ReportRequest.addChildElement("Aggregation",
				"v9");
		Aggregation.addTextNode("Daily");
		SOAPElement Columns = ReportRequest.addChildElement("Columns", "v9");
		SOAPElement KeywordPerformanceReportColumn = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn.addTextNode("TimePeriod");
		SOAPElement KeywordPerformanceReportColumn1 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn1.addTextNode("AccountId");
		SOAPElement KeywordPerformanceReportColumn2 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn2.addTextNode("CampaignId");
		SOAPElement KeywordPerformanceReportColumn3 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn3.addTextNode("Keyword");
		SOAPElement KeywordPerformanceReportColumn4 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn4.addTextNode("KeywordId");
		SOAPElement KeywordPerformanceReportColumn5 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn5.addTextNode("DeviceType");
		SOAPElement KeywordPerformanceReportColumn6 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn6.addTextNode("BidMatchType");
		SOAPElement KeywordPerformanceReportColumn7 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn7.addTextNode("Clicks");
		SOAPElement KeywordPerformanceReportColumn8 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn8.addTextNode("Impressions");
		SOAPElement KeywordPerformanceReportColumn9 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn9.addTextNode("Ctr");
		SOAPElement KeywordPerformanceReportColumn10 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn10.addTextNode("AverageCpc");
		SOAPElement KeywordPerformanceReportColumn11 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn11.addTextNode("Spend");
		SOAPElement KeywordPerformanceReportColumn12 = Columns.addChildElement(
				"KeywordPerformanceReportColumn", "v9");
		KeywordPerformanceReportColumn12.addTextNode("QualityScore");
		SOAPElement Filter = ReportRequest.addChildElement("Filter", "v9");
		Filter.setAttribute("xsi:nil", "true");
		SOAPElement MaxRows = ReportRequest.addChildElement("MaxRows", "v9");
		MaxRows.addTextNode("10");
		SOAPElement Scope = ReportRequest.addChildElement("Scope", "v9");
		SOAPElement AccountIds = Scope.addChildElement("AccountIds", "v9");
		AccountIds.setAttribute("xmlns:a1",
				"http://schemas.microsoft.com/2003/10/Serialization/Arrays");
		SOAPElement Acttypelong = AccountIds.addChildElement("long", "a1");
		Acttypelong.addTextNode(bingAccountId);
		SOAPElement AdGroups = Scope.addChildElement("AdGroups", "v9");
		AdGroups.setAttribute("xsi:nil", "true");
		
		/*SOAPElement Campaigns = Scope.addChildElement("Campaigns", "v9");
		SOAPElement CampaignReportScope = Campaigns.addChildElement(
				"CampaignReportScope", "v9");
		SOAPElement AccountId = CampaignReportScope.addChildElement(
				"AccountId", "v9");
		AccountId.addTextNode(bingAccountId);
		SOAPElement CampaignId = CampaignReportScope.addChildElement(
				"CampaignId", "v9");
		CampaignId.addTextNode(bingCampaignId);*/
		
		SOAPElement Campaigns = Scope.addChildElement("Campaigns", "v9");
		Campaigns.setAttribute("xsi:nil", "true");
		SOAPElement CampaignReportScope = Campaigns.addChildElement(
				"CampaignReportScope", "v9");
		SOAPElement AccountId = CampaignReportScope.addChildElement(
				"AccountId", "v9");
		SOAPElement CampaignId = CampaignReportScope.addChildElement(
				"CampaignId", "v9");
		
		SOAPElement Sort = ReportRequest.addChildElement("Sort", "v9");
		SOAPElement KeywordPerformanceReportSort = Sort.addChildElement(
				"KeywordPerformanceReportSort", "v9");
		SOAPElement SortColumn = KeywordPerformanceReportSort.addChildElement(
				"SortColumn", "v9");
		SortColumn.addTextNode("Clicks");
		SOAPElement SortOrder = KeywordPerformanceReportSort.addChildElement(
				"SortOrder", "v9");
		SortOrder.addTextNode("Ascending");
		SOAPElement Time = ReportRequest.addChildElement("Time", "v9");
		SOAPElement CustomDateRangeEnd = Time.addChildElement(
				"CustomDateRangeEnd", "v9");
		CustomDateRangeEnd.setAttribute("xsi:nil", "true");
		SOAPElement CustomDateRangeStart = Time.addChildElement(
				"CustomDateRangeStart", "v9");
		CustomDateRangeStart.setAttribute("xsi:nil", "true");
		SOAPElement PredefinedTime = Time.addChildElement("PredefinedTime",
				"v9");
		PredefinedTime.addTextNode("Yesterday");
		return envelope;
	}

	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static SOAPEnvelope buildSoapBodyPolling(SOAPEnvelope envelope,
			String reportId) throws SOAPException {
		SOAPBody soapBody = envelope.getBody();
		soapBody.setPrefix("soapenv");

		SOAPElement PollGenerateReportRequest = soapBody.addChildElement(
				"PollGenerateReportRequest", "v9");
		PollGenerateReportRequest.setAttribute("xmlns",
				"https://bingads.microsoft.com/Reporting/v9");
		SOAPElement ReportRequestId = PollGenerateReportRequest
				.addChildElement("ReportRequestId", "v9");
		ReportRequestId.addTextNode(reportId);

		return envelope;
	}

	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static SOAPEnvelope buildSoapHeader(SOAPEnvelope envelope)
			throws SOAPException {
		envelope.getHeader().detachNode();
		SOAPHeader soapHeader = envelope.addHeader();

		SOAPElement CustomerAccountId = soapHeader.addChildElement(
				"CustomerAccountId", "v9");
		CustomerAccountId.addTextNode(bingAccountId);
		SOAPElement CustomerId = soapHeader.addChildElement("CustomerId", "v9");
		CustomerId.addTextNode("128784");
		SOAPElement DeveloperToken = soapHeader.addChildElement(
				"DeveloperToken", "v9");
		DeveloperToken.addTextNode(bingDeveloperToken);
		SOAPElement UserName = soapHeader.addChildElement("UserName", "v9");
		UserName.addTextNode(bingApiUsername);
		SOAPElement Password = soapHeader.addChildElement("Password", "v9");
		Password.addTextNode(bingApiPassword);
		return envelope;
	}

	/*
	 * takes the SOAP message and returns the poll response.
	 */
	
	/**
	 * @return
	 * @param
	 * @throws
	 */
	static String getStatus(SOAPMessage soapMeassage) throws SOAPException {
		return soapMeassage.getSOAPBody().getTextContent();
	}

	/*
	 * takes the report request as a SOAP xml, parses the xml and returns the
	 * download url.
	 */
	
	/**
	 * @return
	 * @param
	 * @throws
	 */
	static String getUrl(SOAPMessage soapMeassage) throws SOAPException {
		String url = soapMeassage.getSOAPBody().getFirstChild().getFirstChild()
				.getFirstChild().getTextContent();
		return url;
	}

	/*
	 * Creates a Soap connection object
	 */
	
	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static SOAPConnection openSoapConnection() throws SOAPException {
		SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory
				.newInstance();
		SOAPConnection soapConnection = soapConnectionFactory
				.createConnection();
		return soapConnection;
	}

	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static SOAPEnvelope openSoapEnvelope(SOAPPart soapPart)
			throws SOAPException {
		SOAPEnvelope envelope = soapPart.getEnvelope();
		envelope.setPrefix("soapenv");
		envelope.addNamespaceDeclaration("v9", serverURI);
		envelope.addNamespaceDeclaration("xsi",
				"http://www.w3.org/2001/XMLSchema-instance");
		return envelope;
	}

	/*
	 * A new SOAPMessage object contains the following by default: A SOAPPart
	 * object A SOAPEnvelope object A SOAPBody object A SOAPHeader object
	 */
	
	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static SOAPMessage openSoapMessage() throws SOAPException {
		MessageFactory messageFactory = MessageFactory.newInstance();
		SOAPMessage soapMessage = messageFactory.createMessage();
		return soapMessage;
	}

	/*
	 * Adds a SOAP part to the SOAPMessage
	 */
	
	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static SOAPPart openSoapPart(SOAPMessage soapMessage) {
		SOAPPart soapPart = soapMessage.getSOAPPart();
		return soapPart;
	}

	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static void PollGenerateReport(MimeHeaders headers,
			SOAPMessage soapMessage) throws Exception {
		headers.addHeader("soapAction", "PollGenerateReport");
		soapMessage.saveChanges();
	}

	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static SOAPMessage PollGenerateReport(String reportID)
			throws RemoteException, Exception {
		SOAPConnection soapConnection1 = openSoapConnection();
		SOAPMessage soapMessage1 = openSoapMessage();
		SOAPPart soapPart1 = openSoapPart(soapMessage1);
		SOAPEnvelope envelope1 = openSoapEnvelope(soapPart1);
		envelope1 = openSoapEnvelope(soapPart1);
		envelope1 = buildSoapHeader(envelope1);
		envelope1 = buildSoapBodyPolling(envelope1, reportID);
		MimeHeaders headers1 = buildMimeHeader(soapMessage1);

		PollGenerateReport(headers1, soapMessage1);
		SOAPMessage soapResponse1 = soapResponse(soapMessage1, soapConnection1);

		return soapResponse1;
	}

	/**
	 * @return
	 * @param
	 * @throws
	 */
	private static void printSOAPResponse(SOAPMessage soapResponse)
			throws Exception {
		TransformerFactory transformerFactory = TransformerFactory
				.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		Source sourceContent = soapResponse.getSOAPPart().getContent();
		StreamResult result = new StreamResult(System.out);
		transformer.transform(sourceContent, result);
	}

	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static SOAPMessage soapResponse(SOAPMessage soapMessage,
			SOAPConnection soapConnection) throws SOAPException {
		return soapConnection.call(soapMessage, url);
	}

	/**
	 * @return
	 * @param
	 * @throws
	 */
	public static void SubmitGenerateReport(MimeHeaders headers,
			SOAPMessage soapMessage) throws Exception {
		headers.addHeader("soapAction", "SubmitGenerateReport");
		soapMessage.saveChanges();
	}
}