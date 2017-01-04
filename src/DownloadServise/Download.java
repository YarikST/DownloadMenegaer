package DownloadServise;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Observable;
import java.util.concurrent.ExecutorService;

/**
 * Created by 2 on 04.05.2016.
 */
public class Download  extends Observable implements Runnable {

  public  enum StatusDownload {
       DOWNLOAD(0),PAUSE(1),STOP(2),ERROR(3), OK(4), ;
        int code;

        StatusDownload(int code) {
            this.code = code;
        }

        @Override
        public String toString() {

            return  name();


        }
    }


    private Thread thread;
    public static final int MAX_SIZE = 128;
    private byte buff[];
    private int size=-1;
    private int rozmirStart=0;
    private StatusDownload status;
    private URL url;
    private static final String myPath = "C:/";
    private RandomAccessFile randomAccessFile;
    private InputStream inputStream;


    public Download(URL url) {
        this.url = url;
        start();
    }

    public   void start() {
        thread = new Thread(this);
        if (isURL()){
            status = StatusDownload.DOWNLOAD;
            thread.start();
            signal();
        } else {
            JOptionPane.showMessageDialog(null,"Помилка запиту перевірте адресу");
            stop();
        }
    }

    public  void stop() {
        status = StatusDownload.STOP;
        thread= null;
        signal();

    }

    public  void pause() {
        status = StatusDownload.PAUSE;
        signal();
    }

    void signal() {
        setChanged();
        notifyObservers();
    }

    public  int getProgres() {
        double d1 = rozmirStart;
        double d2 = size;
        double d3 = d1 / d2;

       return (int) (d3*100d);

    }

    boolean isURL() {


        return url.toString().startsWith("https://")||url.toString().startsWith("http://");
    }

    public   URL getURL() {
        return url;

    }


    @Override
    public void run() {
        try {

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setRequestProperty("Range","bytes="+rozmirStart+"-");
            urlConnection.connect();

            try {

                if (urlConnection.getResponseCode() / 100 != 2) {
                    stop();
                    return;
                }
            } catch (Exception e) {
                System.out.println("error");
                throw  e;
            }


            if (size == -1) {
                size = urlConnection.getContentLength();
                signal();

            }
            String str = url.getFile();
            String path = str.substring(str.lastIndexOf('/')+1,str.lastIndexOf('/')+5);
            String roz = str.substring(str.lastIndexOf('.'));
            randomAccessFile = new RandomAccessFile(myPath + "/"+path+roz, "rw");

            randomAccessFile.seek(rozmirStart);
            inputStream = urlConnection.getInputStream();

            int read = 0;

            while (status == StatusDownload.DOWNLOAD) {
                if (size - rozmirStart > MAX_SIZE) {
                    buff = new byte[MAX_SIZE];
                } else {buff = new byte[size - rozmirStart];}

                read= inputStream.read(buff);
              if (read ==-1||(size - rozmirStart)==0) {
                    break;
              }

                    while (read != buff.length) {
                        int r = buff.length - read;

                        read += inputStream.read(buff, read, r);


                }

                randomAccessFile.write(buff);
                rozmirStart += read;
                signal();

                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (status == StatusDownload.DOWNLOAD) {
                status = StatusDownload.OK;
                signal();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
           if(randomAccessFile !=null)     randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                if(inputStream !=null)     inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public int getSize() {
        return size;

    }

    public StatusDownload getStatus() {
        return status;

    }
}
