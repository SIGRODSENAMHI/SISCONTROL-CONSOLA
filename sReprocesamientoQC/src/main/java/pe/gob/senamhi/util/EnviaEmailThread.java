package pe.gob.senamhi.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class EnviaEmailThread extends Thread {

	private String emailContent;
	private String cordestino;
	
	public EnviaEmailThread(String emailContent, String cordestino) {
		this.emailContent = emailContent;
		this.cordestino = cordestino;
	}

	@Override
	public void run() {
		enviarCorreo();
	}

	private void enviarCorreo(){
		try {
			final String username = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.usuario.sender");
			final String password = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.password.sender");
			final String port = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.port");
			final String host = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.host");
			final String description = PropiedadesUtil.obtenerPropiedad("configuracion", "description.name.correo");
			
//			String[] correos = PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.to.correos").split("\\,");
//			System.out.println(correos);
			String[] correos = cordestino.split("\\,");
			InternetAddress[] addressTo = new InternetAddress[correos.length];
			int cont = 0;
			for(String correo : correos){
				addressTo[cont] = new InternetAddress(correo);
				cont+=1;
			}
			
			InternetAddress[] direccionesTO = addressTo;
	
			Properties props = new Properties();
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.host", host);
			props.put("mail.smtp.port", port);
			//props.put("mail.smtp.host", "outlook.office365.com");
			//props.put("mail.smtp.port", "587");
			//props.put("mail.smtp.port", "25");
	
			Session session = Session.getInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(username, password);
						}
					});

			MimeMessage message = new MimeMessage(session);
			message.addHeader("Content-type", "text/HTML; charset=UTF-8");
			message.addHeader("format", "flowed");
			message.addHeader("Content-Transfer-Encoding", "8bit");
            
			//message.setFrom(new InternetAddress(PropiedadesUtil.obtenerPropiedad("configuracion", "email.sistema.usuario.sender")));
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, direccionesTO);
			message.setSubject(description);
			
			MimeBodyPart messageBodyPart = new MimeBodyPart();

			//Cuerpo en texto
	        messageBodyPart = new MimeBodyPart();
	        Multipart multipart = new MimeMultipart("related");

            messageBodyPart.setContent(emailContent, "text/html");
            multipart.addBodyPart(messageBodyPart);
            
	        message.setContent(multipart);
			Transport.send(message);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}