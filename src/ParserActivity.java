import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;



public class ParserActivity extends JApplet {

	/**
	 * @param args
	 */
	
	 JPanel gui ;
	 JFrame guiFrame ;
	 JTextField urlLabel;
	 JTextField outLabel;
	 String link;
	 JTextArea  textArea;
	 private Entry mEntry;
	public static void main(String[] args) {

		new ParserActivity();
	}

	public ParserActivity() {
		 JFrame guiFrame = new JFrame();
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Parser");
		guiFrame.setSize(450, 300);
		guiFrame.setLocationRelativeTo(null); 
//		
//		
//		btnParse= new Button("Parse");
//		
//		textField = new JTextField();
//		textField.setSize(200, 50);
//		textField.setText("test");
//		textField.setMargin( new Insets(10, 10, 10, 10));
//		guiFrame.add(textField,BorderLayout.NORTH);
//		guiFrame.add(btnParse,BorderLayout.SOUTH);
//		guiFrame.setVisible(true); 
		 JPanel gui = new JPanel(new BorderLayout(2,2));

	        JPanel labelFields = new JPanel(new BorderLayout(2,2));
	        labelFields.setBorder(new TitledBorder(""));

	        JPanel labels = new JPanel(new GridLayout(0,1,1,1));
	        labels.setBorder(new TitledBorder(""));
	        JPanel fields = new JPanel(new GridLayout(0,1,1,1));
	        fields.setBorder(new TitledBorder(""));

	            labels.add(new JLabel("URL :" ));
	            // if these were of different size, it would be necessary to
	            // constrain them using another panel
	            urlLabel=new JTextField(25);
	            fields.add(urlLabel);
	            
	            labels.add(new JLabel("Out Put :" ));
	            // if these were of different size, it would be necessary to
	            // constrain them using another panel
	            outLabel=new JTextField(25);
	            fields.add(outLabel);

	        labelFields.add(labels, BorderLayout.CENTER);
	        labelFields.add(fields, BorderLayout.EAST);

	        JPanel guiCenter = new JPanel(new BorderLayout(2,2));
	        guiCenter.setBorder(new TitledBorder(""));
	        JPanel buttonConstrain = new JPanel(new FlowLayout(FlowLayout.CENTER));
	        buttonConstrain.setBorder(new TitledBorder(""));
	        JButton btn = new JButton("Parser");
	        btn.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
//					System.out.print("");
//					JOptionPane.showMessageDialog(gui, "Eggs are not supposed to be green.");
					processParser();
				}
			});
	        buttonConstrain.add( btn );
	        
	       
	        guiCenter.add( buttonConstrain, BorderLayout.NORTH );
	        textArea= new JTextArea(5,30);
	        guiCenter.add(new JScrollPane(textArea));
	        gui.add(labelFields, BorderLayout.NORTH);
	        gui.add(guiCenter, BorderLayout.CENTER);
	        guiFrame.add(gui);
			guiFrame.setVisible(true); 
			
//	        JOptionPane.showMessageDialog(null, gui);
	}
	
	private void processParser(){
		String url=urlLabel.getText();
		String out = outLabel.getText();
		if(url==null||out==null
				||url.trim().length()==0||out.trim().length()==0
				){
			JOptionPane.showMessageDialog(gui, "Invalid input!");
			return;
		}		
		try{
//			Entry entry=sendGet();
			Entry entry=getFromKite();
//			Entry entry=getQNS();
			textArea.setText(entry.toString());
			
			pullData(entry);

			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
		private Entry sendGet() throws Exception {
			String titleTAG ="<title>";
			String imgTAG="class=\"ck_image\"";
			String linkTAG=".mp3";
			String desTAG="class=\"txt-head\"";
			String timeTAG="dateCreated";
			String url = urlLabel.getText().trim();
			Entry entry = new Entry();
			URL obj = new URL(url);
			System.out.println("\nSending 'GET' request to URL : " + url);

			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream(),"UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				String temp="";
				int index=0;
				if(inputLine.contains(desTAG)){
					index=inputLine.indexOf("TTO -");
					if(index>-1){
						temp=inputLine.substring(index+5);
						temp=temp.replace("</p>", "");
						entry.setDescription(temp);

					}
				}
				if(inputLine.contains(titleTAG)){
					index=inputLine.indexOf("<title>");
					if(index>-1){
						temp=inputLine.substring(index+7);
						temp=temp.replace("</title>", "");
						index=temp.indexOf("-");
						if(index>-1){
							temp=temp.substring(0, index);
						}
						entry.setTitle(temp);

					}
				}
				if(inputLine.contains(imgTAG)){
					index=inputLine.indexOf("src=\"");
					if(index>-1){
						temp=inputLine.substring(index+5);
						temp=temp.replace("\" /></td>", "");
						index=temp.indexOf("jpg");
						if(index>-1){
							temp=temp.substring(0, index+3);
						}
						index=temp.indexOf("jpeg");
						if(index>-1){
							temp=temp.substring(0, index+3);
						}
						entry.setCover(temp);

					}
				}
				if(inputLine.contains(linkTAG)){
					index=inputLine.indexOf("file: \"");
					if(index>-1){
						temp=inputLine.substring(index+7);
						temp=temp.replace("\",", "");
						entry.setLink(temp);

					}
				}
				if(inputLine.contains(timeTAG)){
					index=inputLine.indexOf("content=\"");
					if(index>-1){
						temp=inputLine.substring(index+9);
						index=temp.indexOf("\"");
						if(index>-1){
							temp=temp.substring(0, index);
						}
						index=temp.indexOf("+");
						if(index>-1){
							temp=temp.substring(0, index);
						}
						entry.setTime(temp);
					}
				}
//				if(inputLine.contains(desTAG)
//						||inputLine.contains(titleTAG)
//						||inputLine.contains(imgTAG)
//						||inputLine.contains(linkTAG)){
//					link=inputLine;
//					response.append(inputLine.trim()+"\n");
//
//				}
			}
			in.close();
	 
			//print result
//			System.out.println(response.toString());

			return entry;
	 
		}
		private Entry getFromKite() throws Exception {
			String titleTAG ="og:title";
			String imgTAG="og:image";
			String linkTAG="contentUrl";
			String desTAG="itemprop=\"description\"";
			String timeTAG="article:published_time";
			String url = urlLabel.getText().trim();
			Entry entry = new Entry();
			URL obj = new URL(url);
			System.out.println("\nSending 'GET' request to URL : " + url);

			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream(),"UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				String temp="";
				int index=0;
				if(inputLine.contains(desTAG)){
					index=inputLine.indexOf("content=\"");
					if(index>-1){
						temp=inputLine.substring(index+9);
						temp=temp.replace("\">", "");
						entry.setDescription(temp);

					}
				}
				if(inputLine.contains(titleTAG)){
					index=inputLine.indexOf("content=\"");
					if(index>-1){
						temp=inputLine.substring(index+9);
						temp=temp.replace("\" />", "");
//						index=temp.indexOf("-");
//						if(index>-1){
//							temp=temp.substring(0, index);
//						}
						entry.setTitle(temp);

					}
				}
				if(inputLine.contains(imgTAG)){
					index=inputLine.indexOf("content=\"");
					if(index>-1){
						temp=inputLine.substring(index+9);
						temp=temp.replace("\" /></td>", "");
						index=temp.indexOf("jpg");
						if(index>-1){
							temp=temp.substring(0, index+3);
						}
						index=temp.indexOf("jpeg");
						if(index>-1){
							temp=temp.substring(0, index+3);
						}
						entry.setCover(temp);

					}
				}
				if(inputLine.contains(linkTAG)){
					index=inputLine.indexOf("content=\"");
					if(index>-1){
						temp=inputLine.substring(index+9);
						temp=temp.replace("\" />", "");
						entry.setLink(temp);

					}
				}
				if(inputLine.contains(timeTAG)){
					index=inputLine.indexOf("content=\"");
					if(index>-1){
						temp=inputLine.substring(index+9);
						index=temp.indexOf("\"");
						if(index>-1){
							temp=temp.substring(0, index);
						}
						index=temp.indexOf("+");
						if(index>-1){
							temp=temp.substring(0, index);
						}
						temp=temp.replace("\" />", "");
						entry.setTime(temp);
					}
				}
//				if(inputLine.contains(desTAG)
//						||inputLine.contains(titleTAG)
//						||inputLine.contains(imgTAG)
//						||inputLine.contains(linkTAG)){
//					link=inputLine;
//					response.append(inputLine.trim()+"\n");
//
//				}
			}
			in.close();
	 
			//print result
//			System.out.println(response.toString());

			return entry;
	 
		}
	
		private Entry getQNS() throws Exception {
			String titleTAG ="Quick & Snow";
			String imgTAG="Upload/Images/Chuongtrinh/";
			String linkTAG="file=Upload";
			String desTAG="itemprop=\"description\"";
			String timeTAG="article:published_time";
			String url = urlLabel.getText().trim();
			Entry entry = new Entry();
			URL obj = new URL(url);
			System.out.println("\nSending 'GET' request to URL : " + url);

			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// optional default is GET
			con.setRequestMethod("GET");
	 
			//add request header
	 
			int responseCode = con.getResponseCode();
			System.out.println("\nSending 'GET' request to URL : " + url);
			System.out.println("Response Code : " + responseCode);
	 
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream(),"UTF-8"));
			String inputLine;
			StringBuffer response = new StringBuffer();
	 
			while ((inputLine = in.readLine()) != null) {
				String temp="";
				int index=0;
				if(inputLine.contains(desTAG)){
					index=inputLine.indexOf("content=\"");
					if(index>-1){
						temp=inputLine.substring(index+9);
						temp=temp.replace("\">", "");
						entry.setDescription(temp);

					}
				}
				if(inputLine.contains(titleTAG)){
					index=inputLine.indexOf("content=\"");
					if(index>-1){
						temp=inputLine.substring(index+9);
						temp=temp.replace("\" />", "");
//						index=temp.indexOf("-");
//						if(index>-1){
//							temp=temp.substring(0, index);
//						}
					

					}
					entry.setTitle(inputLine);
				}
				if(inputLine.contains(imgTAG)){
					index=inputLine.indexOf("Upload");
					if(index>-1){
						temp=inputLine.substring(index);
						temp=temp.replace("\" /></td>", "");
						index=temp.indexOf("jpg");
						if(index>-1){
							temp=temp.substring(0, index+3);
						}
						index=temp.indexOf("jpeg");
						if(index>-1){
							temp=temp.substring(0, index+3);
						}
						String imgUrl="http://qns.vn/"+temp;
						entry.setCover(imgUrl);

					}
				}
				if(inputLine.contains(linkTAG)){
					index=inputLine.indexOf("Upload");
					if(index>-1){
						temp=inputLine.substring(index);
						index=temp.indexOf(".mp3");
						if(index>-1){
							temp=temp.substring(0, index+4);
						}
//						String imgUrl="http://qns.vn/Upload/Images/Chuongtrinh/avachuongtrin.jpg";
//						entry.setCover(imgUrl);
						temp="http://qns.vn/"+temp;
						entry.setLink(temp);

					}
				}
				if(inputLine.contains(timeTAG)){
					index=inputLine.indexOf("content=\"");
					if(index>-1){
						temp=inputLine.substring(index+9);
						index=temp.indexOf("\"");
						if(index>-1){
							temp=temp.substring(0, index);
						}
						index=temp.indexOf("+");
						if(index>-1){
							temp=temp.substring(0, index);
						}
						temp=temp.replace("\" />", "");
						entry.setTime(temp);
					}
				}
//				if(inputLine.contains(desTAG)
//						||inputLine.contains(titleTAG)
//						||inputLine.contains(imgTAG)
//						||inputLine.contains(linkTAG)){
//					link=inputLine;
//					response.append(inputLine.trim()+"\n");
//
//				}
			}
			in.close();
	 
			//print result
//			System.out.println(response.toString());

			return entry;
	 
		}
	
		
		private void parseData(){
			
			String title ="<title>";
			
		}
		
		private void pullData(Entry entry){
			String temp="https://api.parse.com/1/classes/Entry";
			
			try {
				URL url= new URL(temp);
				HttpsURLConnection con =(HttpsURLConnection) url.openConnection();
				con.setRequestProperty("X-Parse-Application-Id", "574rMj7tZE1RgwpEHaqgAhE8AjYyrm7EGjDDEwVT");
				con.setRequestProperty("X-Parse-REST-API-Key", "SHahCE27rZyI7IDu2bjYIM3NTwaZcG5h6sE6gBhb");
				con.setRequestProperty("Content-Type", "application/json");
				con.setRequestMethod("POST");
				
				con.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.write(entry.toString().getBytes("UTF-8"));
				wr.flush();
				wr.close();
		 
				int responseCode = con.getResponseCode();
				System.out.println("\nSending 'POST' request to URL : " + url.toString());
				System.out.println("Post parameters : " + entry.toString());
				System.out.println("Response Code : " + responseCode);
		 
				BufferedReader in = new BufferedReader(
				        new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
		 
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
		 
				//print result
				System.out.println(response.toString());
		 
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
}
