package controller.noti;

import jakarta.servlet.annotation.WebServlet;
import model.noti.NotificationDto;

@WebServlet("")
public class NotiCommentServlet {
	NotificationDto notiDto = new NotificationDto();
	
	Long notiRecipientNum = notiDto.getNotiRecipientNum();
	Long notiSenderNum = notiDto.getNotiSenderNum();
	int notiTypeCode = notiDto.getNotiTypeCode();
	int notiTargetTypeCode = notiDto.getNotiTargetTypeCode();
	Long notiTargetNum = notiDto.getNotiTargetNum();
	String notiMessage = notiDto.getNotiMessage();
}