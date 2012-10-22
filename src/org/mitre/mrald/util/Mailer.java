/*
 *  Copyright 2008 The MITRE Corporation (http://www.mitre.org/). All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mitre.mrald.util;

import java.io.InputStream;

import java.net.InetAddress;

import java.nio.charset.Charset;


import org.columba.ristretto.composer.*;
import org.columba.ristretto.io.*;
import org.columba.ristretto.message.*;
import org.columba.ristretto.parser.*;
import org.columba.ristretto.smtp.*;
//  import org.mitre.mrald.util.MraldOutFile;


/**
 * This class uses the low level Java mail library from here:
 * http://www.columbamail.org/drupal/ to allow for the sending of email
 *
 * @author jchoyt
 */
public class Mailer
{

    /**
     * Description of the Method
     *
     * @param to          Description of Parameter
     * @param from        Description of Parameter
     * @param smtpServer  Description of the Parameter
     * @param body        Description of the Parameter
     * @param subject     Description of Parameter
     * @since 3.1
     */
    public static void send(String to, String from, String smtpServer, String body, String subject)
    {

        if (body == null)
        {
            body = "";
        }

        try
        {

            // Parse and check the given to- and from-address
            Address fromAddress;
            fromAddress = AddressParser.parseAddress(from);

            Address toAddress;
            toAddress = AddressParser.parseAddress(to);

            // First we compose the message and then we connect to the SMTP
            // server to send it.
            // PART 1 : Composing a message
            // Header is the actual header while BasicHeader wraps
            // a Header object to give easy access to the Header.
            Header header = new Header();
            BasicHeader basicHeader = new BasicHeader(header);

            // Add the fields to the header
            // Note that the basicHeader is only a convienience wrapper
            // for our header object.
            basicHeader.setFrom(fromAddress);
            basicHeader.setTo(new Address[] { toAddress });
            basicHeader.setSubject(subject, Charset.forName("ISO-8859-1"));
            basicHeader.set("X-Mailer", "SimpleSMTP example / Ristretto API");

            // Now a mimepart is prepared which actually holds the message
            // The mimeHeader is another convienice wrapper for the header
            // object
            MimeHeader mimeHeader = new MimeHeader(header);
            mimeHeader.set("Mime-Version", "1.0");

            LocalMimePart root = new LocalMimePart(mimeHeader);
            LocalMimePart textPart;
            textPart = root;

            // Now we can add some message text
            MimeHeader textHeader = textPart.getHeader();
            textHeader.setMimeType(new MimeType("text", "plain"));

            // a simple string
            root.setBody(new CharSequenceSource(body));

            InputStream messageSource;
            // Finally we render the message to an inputstream
            messageSource = MimeTreeRenderer.getInstance().renderMimePart(root);
            // Part 2 : Sending the message
            //Construct the proctol that is bound to the SMTP server
            SMTPProtocol protocol = new SMTPProtocol(smtpServer);

            // Open the port
            protocol.openPort();

            //say hello, if necessary
            try
            {
                protocol.ehlo( InetAddress.getByName( smtpServer ) );
            }
            catch (Exception e)
            {
                e.printStackTrace();
                //well, it didn't like ehlo, try helo
                try
                {
                    protocol.helo( InetAddress.getByName( smtpServer ) );
                }
                catch (Exception ee)
                {
                    //didn't like that either - try going on without it
                    e.printStackTrace();
                }
            }


            // Setup from and recipient
            protocol.mail(fromAddress);
            protocol.rcpt(toAddress);

            // Finally send the data
            protocol.data(messageSource);

            // And close the session
            protocol.quit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // TODO: method for sending file, options for encryption

    /*
     *  public static void sendFile( String to, String from, String host, String filename, String msgText1, String subject )
     *  {
     *  // create some properties and get the default Session
     *  Properties props = System.getProperties();
     *  props.put("mail.smtp.host", host);
     *  Session session = Session.getDefaultInstance( props, null );
     *  //        session.setDebug( debug );
     *  try
     *  {
     *  // create a message
     *  MimeMessage msg = new MimeMessage( session );
     *  msg.setFrom( new InternetAddress( from ) );
     *  InternetAddress[] address = {new InternetAddress( to )};
     *  msg.setRecipients( Message.RecipientType.TO, address );
     *  msg.setSubject( subject );
     *  msg.setHeader( "X-Mailer", "MyCgiServer" );
     *  msg.setHeader( "Content-Type", "text/plain; charset=\"ISO-8859-1\"" );
     *  // create and fill the first message part
     *  MimeBodyPart mbp1 = new MimeBodyPart();
     *  mbp1.setText( msgText1 );
     *  // create the second message part
     *  MimeBodyPart mbp2 = new MimeBodyPart();
     *  // attach the file to the message
     *  FileDataSource fds = new FileDataSource( filename );
     *  mbp2.setDataHandler( new DataHandler( fds ) );
     *  mbp2.setFileName( fds.getName() );
     *  // create the Multipart and its parts to it
     *  Multipart mp = new MimeMultipart();
     *  mp.addBodyPart( mbp1 );
     *  mp.addBodyPart( mbp2 );
     *  // add the Multipart to the message
     *  msg.setContent( mp );
     *  // set the Date: header
     *  msg.setSentDate( new Date() );
     *  // send the message
     *  Transport.send( msg );
     *  }
     *  catch( MessagingException mex )
     *  {
     *  mex.printStackTrace();
     *  Exception ex = null;
     *  if( ( ex = mex.getNextException() ) != null )
     *  {
     *  ex.printStackTrace();
     *  }
     *  }
     *  }
     */
    /*
     *  Usage example
     *  public static void main(String args[])
     *  {
     *  String to = "jchoyt@mitre.org";
     *  String from = "mailer";
     *  String subject = "Test";
     *  String text = "testing\n testingn\n\ttesting";
     *  String host = "mail.mitre.org";
     *  //send( String to, String from, String host, String text, String subject )
     *  try
     *  {
     *  Mailer.send(to, from, host, text, subject);
     *  String file = "c:\\devel\\mrald\\mraldlog.log";
     *  //sendFile( String to, String from, String host, String filename, String msgText1, String subject )
     *  Mailer.sendFile(to, from, host, file, text, subject);
     *  }
     *  catch (Exception e)
     *  {
     *  System.out.println("failed " + e.toString());
     *  }
     *  }
     */
}

/*
 *  ***** BEGIN LICENSE BLOCK *****
 *  The contents of this file are subject to the the GNU Lesser General Public
 *  License Version 2.1 or later (the "LGPL").
 *
 *  Software distributed under the License is distributed on an "AS IS" basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *  The Original Code is Ristretto Mail API.
 *
 *  The Initial Developers of the Original Code are
 *  Timo Stich and Frederik Dietz.
 *  Portions created by the Initial Developers are Copyright (C) 2004
 *  All Rights Reserved.
 *
 *  ***** END LICENSE BLOCK *****
 */
