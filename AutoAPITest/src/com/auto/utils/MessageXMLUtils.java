package com.auto.utils;

public class MessageXMLUtils {

//	public static void writeXML(MessageReport report, String filePath) {
//		try
//		{
//			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
//			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
//
//			// root elements
//			Document doc = docBuilder.newDocument();
//			Element rootElement = doc.createElement("message-report");
//			doc.appendChild(rootElement);
//
//			// summary element
//			Element summary = doc.createElement("summary-of-today");
//			rootElement.appendChild(summary);
//
//			// Today
//			Element today = doc.createElement("date");
//			today.appendChild(doc.createTextNode("" + LocalDate.now()));
//			summary.appendChild(today);
//			// total totalsent
//			Element totalsent = doc.createElement("total-sent");
//			totalsent.appendChild(doc.createTextNode("" + report.getMessageDataOfToday().getTotalSend()));
//			summary.appendChild(totalsent);
//			// total totalreceived
//			Element totalreceived = doc.createElement("total-recieved");
//			totalreceived.appendChild(doc.createTextNode("" + report.getMessageDataOfToday().getTotalReceived()));
//			summary.appendChild(totalreceived);
//			// total totaldelayed
//			Element totaldelayed = doc.createElement("total-delayed");
//			totaldelayed.appendChild(doc.createTextNode("" + report.getMessageDataOfToday().getTotalDelayed()));
//			summary.appendChild(totaldelayed);
//			// Average delay
//			Element averageDelay = doc.createElement("average-delayed");
//			averageDelay.appendChild(doc.createTextNode("" + report.getMessageDataOfToday().getAverageDelay()));
//			summary.appendChild(averageDelay);
//			// total totalexpired
//			Element totalexpired = doc.createElement("total-expired");
//			totalexpired.appendChild(doc.createTextNode("" + report.getMessageDataOfToday().getTotalExpired()));
//			summary.appendChild(totalexpired);
//			// total totalpending
//			Element totalpending = doc.createElement("total-pending");
//			totalpending.appendChild(doc.createTextNode("" + report.getMessageDataOfToday().getTotalPending()));
//			summary.appendChild(totalpending);
//
//			// summary-3 hour element
//			Element summaryOfLast3Hour = doc.createElement("summary-last-three-hours");
//			rootElement.appendChild(summaryOfLast3Hour);
//			// total totalsent 
//			Element totalsentOfLast3Hour = doc.createElement("total-sent");
//			totalsentOfLast3Hour.appendChild(doc.createTextNode("" + report.getMessageDataOfLast3Hour().getTotalSend()));
//			summaryOfLast3Hour.appendChild(totalsentOfLast3Hour);
//			// total totalreceived OfLast3Hour()
//			Element totalreceivedOfLast3Hour = doc.createElement("total-recieved");
//			totalreceivedOfLast3Hour.appendChild(doc.createTextNode("" + report.getMessageDataOfLast3Hour().getTotalReceived()));
//			summaryOfLast3Hour.appendChild(totalreceivedOfLast3Hour);
//			// total totaldelayed OfLast3Hour()
//			Element totaldelayedOfLast3Hour = doc.createElement("total-delayed");
//			totaldelayedOfLast3Hour.appendChild(doc.createTextNode("" + report.getMessageDataOfLast3Hour().getTotalDelayed()));
//			summaryOfLast3Hour.appendChild(totaldelayedOfLast3Hour);
//			// Average delay
//			Element averageDelayOfLast3Hour = doc.createElement("average-delayed");
//			averageDelayOfLast3Hour.appendChild(doc.createTextNode("" + report.getMessageDataOfLast3Hour().getAverageDelay()));
//			summaryOfLast3Hour.appendChild(averageDelayOfLast3Hour);
//			// total totalexpired OfLast3Hour()
//			Element totalexpiredOfLast3Hour = doc.createElement("total-expired");
//			totalexpiredOfLast3Hour.appendChild(doc.createTextNode("" + report.getMessageDataOfLast3Hour().getTotalExpired()));
//			summaryOfLast3Hour.appendChild(totalexpiredOfLast3Hour);
//			// total totalpending OfLast3Hour()
//			Element totalpendingOfLast3Hour = doc.createElement("total-pending");
//			totalpendingOfLast3Hour.appendChild(doc.createTextNode("" + report.getMessageDataOfLast3Hour().getTotalPending()));
//			summaryOfLast3Hour.appendChild(totalpendingOfLast3Hour);
//
//
//			// summary-accumulated element
//			Element summaryAccumulated = doc.createElement("summary-accumulated");
//			rootElement.appendChild(summaryAccumulated);
//			// total totalsent Accumulated()
//			Element totalsentAccumulated = doc.createElement("total-sent");
//			totalsentAccumulated.appendChild(doc.createTextNode("" + report.getMessageDataAccumulated().getTotalSend()));
//			summaryAccumulated.appendChild(totalsentAccumulated);
//			// total totalreceived Accumulated()
//			Element totalreceivedAccumulated = doc.createElement("total-recieved");
//			totalreceivedAccumulated.appendChild(doc.createTextNode("" + report.getMessageDataAccumulated().getTotalReceived()));
//			summaryAccumulated.appendChild(totalreceivedAccumulated);
//			// total totaldelayed Accumulated()
//			Element totaldelayedAccumulated = doc.createElement("total-delayed");
//			totaldelayedAccumulated.appendChild(doc.createTextNode("" + report.getMessageDataAccumulated().getTotalDelayed()));
//			summaryAccumulated.appendChild(totaldelayedAccumulated);
//			// Average delay
//			Element averageDelayAccumulated = doc.createElement("average-delayed");
//			averageDelayAccumulated.appendChild(doc.createTextNode("" + report.getMessageDataAccumulated().getAverageDelay()));
//			summaryAccumulated.appendChild(averageDelayAccumulated);
//			// total totalexpired Accumulated()
//			Element totalexpiredAccumulated = doc.createElement("total-expired");
//			totalexpiredAccumulated.appendChild(doc.createTextNode("" + report.getMessageDataAccumulated().getTotalExpired()));
//			summaryAccumulated.appendChild(totalexpiredAccumulated);
//			// total totalpending Accumulated()
//			Element totalpendingAccumulated = doc.createElement("total-pending");
//			totalpendingAccumulated.appendChild(doc.createTextNode("" + report.getMessageDataAccumulated().getTotalPending()));
//			summaryAccumulated.appendChild(totalpendingAccumulated);
//
//
//			// delayed elements
//			Element delayedlist = doc.createElement("delayed-list");
//			rootElement.appendChild(delayedlist);
//
//			// message elements
//			Element messagelist = doc.createElement("message-list");
//			rootElement.appendChild(messagelist);
//
//			// script element
//			Element message;
//			Element messageid;
//			Element recipientlist;
//			Element recipient;
//			Element recipientid;
//			Element senddatetime;
//			Element acknowledgedatetime;
//			Element delaytime;
//			Element status;
//			Element hotelroomnumber;
//
//			List<Message> messages = report.getMessagesOfToday();
//			for(Message msg : messages)
//			{
//				message = doc.createElement("message");
//				messagelist.appendChild(message);
//
//				messageid = doc.createElement("message-id");
//				messageid.appendChild(doc.createTextNode("" + msg.getMessage_id()));
//				message.appendChild(messageid);
//
//				recipientlist = doc.createElement("recipient-list");
//				message.appendChild(recipientlist);
//
//				for(MessageInfo msginfo : msg.getMessageInfoList())
//				{
//					for(MessageRecipient msgrecipient : msginfo.getMessageRecipientList())
//					{
//						recipient = doc.createElement("recipient");
//						recipientlist.appendChild(recipient);
//
//						recipientid = doc.createElement("recipient-id");
//						recipientid.appendChild(doc.createTextNode("" + msgrecipient.getMessage_recipient_id()));
//						recipient.appendChild(recipientid);
//
//						hotelroomnumber  = doc.createElement("hotel-room-number");
//						hotelroomnumber.appendChild(doc.createTextNode("" + msgrecipient.getHotel_room_number()));
//						recipient.appendChild(hotelroomnumber);
//
//						status = doc.createElement("status");
//						status.appendChild(doc.createTextNode("" + msgrecipient.getStatus()));
//						recipient.appendChild(status);
//
//						senddatetime = doc.createElement("send-datetime");
//						senddatetime.appendChild(doc.createTextNode("" + msgrecipient.getSend_datetime()));
//						recipient.appendChild(senddatetime);
//
//						acknowledgedatetime = doc.createElement("acknowledge-datetime");
//						acknowledgedatetime.appendChild(doc.createTextNode("" + msgrecipient.getAcknowledge_datetime()));
//						recipient.appendChild(acknowledgedatetime);
//
//						delaytime = doc.createElement("delay-time");
//						delaytime.appendChild(doc.createTextNode("" + msgrecipient.getDelayedTime()));
//						recipient.appendChild(delaytime);
//
//						// Add to delay list
//						if(msgrecipient.isUnexpectedDelayed())
//						{
//							recipient = doc.createElement("recipient");
//							recipientlist.appendChild(recipient);
//
//							recipientid = doc.createElement("recipient-id");
//							recipientid.appendChild(doc.createTextNode("" + msgrecipient.getMessage_recipient_id()));
//							recipient.appendChild(recipientid);
//
//							hotelroomnumber  = doc.createElement("hotel-room-number");
//							hotelroomnumber.appendChild(doc.createTextNode("" + msgrecipient.getHotel_room_number()));
//							recipient.appendChild(hotelroomnumber);
//
//							status = doc.createElement("status");
//							status.appendChild(doc.createTextNode("" + msgrecipient.getStatus()));
//							recipient.appendChild(status);
//
//							senddatetime = doc.createElement("send-datetime");
//							senddatetime.appendChild(doc.createTextNode("" + msgrecipient.getSend_datetime()));
//							recipient.appendChild(senddatetime);
//
//							acknowledgedatetime = doc.createElement("acknowledge-datetime");
//							acknowledgedatetime.appendChild(doc.createTextNode("" + msgrecipient.getAcknowledge_datetime()));
//							recipient.appendChild(acknowledgedatetime);
//
//							delaytime = doc.createElement("delay-time");
//							delaytime.appendChild(doc.createTextNode("" + msgrecipient.getDelayedTime()));
//							recipient.appendChild(delaytime);
//
//							delayedlist.appendChild(recipient);
//						}
//					}
//				}
//			}
//
//			// write the content into xml file
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			Transformer transformer = transformerFactory.newTransformer();
//			transformer.setOutputProperty(javax.xml.transform.OutputKeys.INDENT, "yes");
//			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
//
//			DOMSource source = new DOMSource(doc);
//			StreamResult result = new StreamResult(new File(filePath));
//
//			transformer.transform(source, result);
//
//			System.out.println("File saved! - Path: " + filePath);
//
//		} catch (ParserConfigurationException pce) {
//			pce.printStackTrace();
//		} catch (TransformerException tfe) {
//			tfe.printStackTrace();
//		}
//	}
}

