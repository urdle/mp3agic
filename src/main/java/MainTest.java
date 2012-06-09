import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v2Frame;
import com.mpatric.mp3agic.ID3v2FrameSet;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;


public class MainTest {

	        public static void main(String args[])
	        {
	            //System.out.println("Hello World!");
	            try {
	            	RandomAccessFile sample = new RandomAccessFile("C:\\Users\\Michael\\workspace\\mp3agic-urdle\\test-sample.txt","r");
	            	String oneName;
	            	Mp3File mp3file=null;
	            	while ((oneName=sample.readLine()) != null)
	            	{
	             		if ( !oneName.startsWith("#")) {
	             	 		if ( !oneName.isEmpty()) {
	    	            		mp3file=new Mp3File(oneName,false);
	             	 		}
	            		}
	    	            		
	            	}
	            	
//	            	Mp3File mp3file1=new Mp3File("C:\\Users\\Michael\\Music\\Genesis\\(1969) Genesis To Revelation\\03. Where The Sour Turns To Sweet.mp3",65536,false);
//	            	Mp3File mp3file2=new Mp3File("C:\\Users\\Michael\\Music\\Divers\\Avril Lavigne - Complicated.mp3",65536,false);
//	            	Mp3File mp3file3=new Mp3File("C:\\Users\\Michael\\Music\\B.O\\The Legend of Zelda- 25th Anniversary Symphony\\08. Ballad of the Goddess (Various Artists).mp3",65536,false);
//	            	Mp3File mp3file4=new Mp3File("C:\\Users\\Michael\\Documents\\ziik\\Music\\THE CLASSIC\\O.S.T\\Cello Concerto Rv424 B Minor.mp3",65536,false);
//	            	C:\Users\Michael\Documents\ziik\08. The Minstrel Boy (Instrumental) .flac
//	            	C:\Users\Michael\Documents\ziik\He's A Pirate-b-80pc.wav
//	            	C:\Users\Michael\Documents\ziik\Music\Bob McKaty, Pierre-Alexis Charles\Dancing with celts\14. Paper Plate.wma
//	            	C:\Users\Michael\Documents\ziik\Music\R.E.M\Accelerate\07. Until The Day Is Done.WMA
	            	
	            	//ID3v2 v2tag=mp3file.getId3v2Tag();
	            	//APEv2 apetag=mp3file.getAPEv2Tag();
	            	//ID3v1 v1tag=mp3file.getId3v1Tag();
	            	Map<String, ID3v2FrameSet> v2fs=mp3file.getId3v2Tag().getFrameSets();
	            	Set<Entry<String, ID3v2FrameSet>> se=v2fs.entrySet();
	            	Iterator<Entry<String, ID3v2FrameSet>> ie=se.iterator();
	            	 while(ie.hasNext())  {
	            		Entry<String, ID3v2FrameSet> ee=ie.next();
	            		ID3v2FrameSet oneFS=ee.getValue();
	            		Iterator<ID3v2Frame> ifr=oneFS.getFrames().iterator();
		               	 while(ifr.hasNext())  {
		               		ID3v2Frame fr=ifr.next();
		               		String str=new String(fr.getData(), 0,  fr.getDataLength(), "ISO-8859-1");
		    				System.out.println("Value: //" +ee.getKey().toString() + "//" + fr.getDataLength() + "//" + str + "#");
		               	 }
	            	}
	            	
//	            	v2fs.get("TRCK").getFrames().get(0).getData();
//					System.out.println("Title: " + v2fs.toString());
//					System.out.println("CustomTags: " + mp3file.getCustomTag());
				} catch (UnsupportedTagException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidDataException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	        }


}
