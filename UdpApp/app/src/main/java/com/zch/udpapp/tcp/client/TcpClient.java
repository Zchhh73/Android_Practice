package com.zch.udpapp.tcp.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {

    private Scanner in;

    public TcpClient() {
        in = new Scanner(System.in);
        in.useDelimiter("\n");
    }

    public void start(){
        try {
            Socket socket = new Socket("192.168.246.2",9090);
            InputStream is = socket.getInputStream();
            OutputStream os=socket.getOutputStream();

            final BufferedReader br = new BufferedReader(new InputStreamReader(is));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));

            //从server端读数据,输出
            new Thread(){
                @Override
                public void run() {
                    try{
                        String line = null;
                        while((line = br.readLine())!=null){
                            System.out.println(line);
                        }
                    }catch(IOException e){
                    }
                }
            }.start();

            while(true){
                String msg=in.next();
                bw.write(msg);
                bw.newLine();
                bw.flush();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
