package org.example;

import java.net.*;
import java.io.*;
import java.util.Objects;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(36000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 36000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        boolean str = true;
        while (str) {
            try {
                System.out.println("Listo para recibir ...");
                clientSocket = serverSocket.accept();
            } catch (IOException e) {
                System.err.println("Accept failed.");
                System.exit(1);
            }
            PrintWriter out = new PrintWriter(
                    clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            String inputLine, outputLine = null;
            boolean firstLine = true;
            String request = "";
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Recibí: " + inputLine);
                if (firstLine) {
                    String[] firstlist = inputLine.split(" ");
                    try {
                        request = firstlist[1].split("=")[1];
                        request = request.split("\\(")[1];
                        request = request.split("\\)")[0];
                    } catch (Exception e) {}
                    firstLine = false;
                }if (!in.ready()) {
                    break;
                }
            }
            if (!Objects.equals(request, "")){
                try{
                    String[] requests = request.split(",");
                    if (requests.length == 1){
                        outputLine = getHello() + getClass(request);
                    }
                    //intento de los otros metodos
                    //    else if (requests.length == 2){
                    //      outputLine = getHello() + invoke(request);
                    //}
                    //else if (requests.length==3){
                    //  outputLine = getHello() + unaryInvoke(request)
                    //}
                }catch (Exception e){
                    e.printStackTrace();
                }
            }else {
                outputLine = getHello() + indexHtml();
            }

            out.println(outputLine);
            out.close();
            in.close();
        }
        clientSocket.close();
        serverSocket.close();
    }



    //getClass corrieno bien
   public static String getClass(String clase) throws ClassNotFoundException {
       Class<?> c = Class.forName(clase);
       return c.getName().toString();
   }

   //public static String unaryInvoke (String parametro){
   //     return "";
   //}

   public static String invoke (String parametro){
        return "";
   }


    public static String indexHtml()
    {
        return  "<!DOCTYPE html>\n" +
                "<html>\n" +
                "    <head>\n" +
                "        <title>Form Example</title>\n" +
                "        <meta charset=\"UTF-8\">\n" +
                "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    </head>\n" +
                "    <body>\n" +
                "        <h1>Form with GET</h1>\n" +
                "        <form action=\"/hello\">\n" +
                "            <label for=\"name\">Name:</label><br>\n" +
                "            <input type=\"text\" id=\"name\" name=\"name\" value=\"John\"><br><br>\n" +
                "            <input type=\"button\" value=\"Submit\" onclick=\"loadGetMsg()\">\n" +
                "        </form> \n" +
                "        <div id=\"getrespmsg\"></div>\n" +
                "\n" +
                "        <script>\n" +
                "            function loadGetMsg() {\n" +
                "                let nameVar = document.getElementById(\"name\").value;\n" +
                "                const xhttp = new XMLHttpRequest();\n" +
                "                xhttp.onload = function() {\n" +
                "                    document.getElementById(\"getrespmsg\").innerHTML =\n" +
                "                    this.responseText;\n" +
                "                }\n" +
                "                xhttp.open(\"GET\", \"/hello?name=\"+nameVar);\n" +
                "                xhttp.send();\n" +
                "            }\n" +
                "        </script>\n" +
                "\n" +
                "    </body>\n" +
                "</html>";
    }

    public static String getHello(){
        return "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/html\r\n"
                + "\r\n";
    }



}