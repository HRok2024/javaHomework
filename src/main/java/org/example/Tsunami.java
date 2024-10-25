package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class Tsunami {

    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) throws IOException, ParseException {
        StringBuilder urlString = new StringBuilder("ㅁㄹ");
        urlString.append("?" + "serviceKey" + "ㅁㄹ");
        urlString.append("&" + "pageNo=1");
        urlString.append("&" + "numOfRows=670");
        urlString.append("&" + "type=JSON");

        URL url = new URL(urlString.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader br;
        if (conn.getResponseCode() == 200) {
            br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        } else {
            br = new BufferedReader(new InputStreamReader(conn.getErrorStream(), "UTF-8"));
        }
        String result = br.readLine();
        br.close();
        conn.disconnect();

        JSONParser jp = new JSONParser();
        JSONObject jsonObject = (JSONObject) jp.parse(result);
        JSONArray arr = (JSONArray) jsonObject.get("TsunamiShelter");
        JSONObject ob = (JSONObject) arr.get(1);
        JSONArray arr2 = (JSONArray) ob.get("row");


        System.out.println("해일대피소 찾기 => 시, 도를 입력해주세요 :");
        String sido = sc.nextLine();
        int i = 1;
        for (Object o : arr2) {
            JSONObject obj = (JSONObject) o;
            if (obj.get("sido_name").toString().equals(sido)) {
                System.out.print(i + "\t");
                System.out.print(obj.get("shel_nm") + " / ");
                System.out.print(obj.get("shel_div_type") + " / ");
                System.out.print(obj.get("address") + " / ");
                System.out.print(obj.get("manage_gov_nm") + " / ");
                System.out.println(obj.get("tel"));
                i++;
            }
        }
        System.out.println("");
        System.out.println("검색결과에서 원하는 관리 기관을 입력해주세요(시청, 군청, ...) : ");
        System.out.println("");
        String gov = sc.nextLine();
        int j = 1;
        for (Object o1 : arr2) {
            JSONObject obj1 = (JSONObject) o1;
            if (obj1.get("manage_gov_nm").toString().equals(gov)) {
                System.out.print(j + "\t");
                System.out.print(obj1.get("shel_nm") + " / ");
                System.out.print(obj1.get("shel_div_type") + " / ");
                System.out.print(obj1.get("address") + " / ");
                System.out.print(obj1.get("manage_gov_nm") + " / ");
                System.out.println(obj1.get("tel"));
                j++;
            }
        }
        System.out.println("");
        System.out.println("검색결과에서 원하는 대피소를 입력해주세요 : ");
        System.out.println("");
        String nm = sc.nextLine();
        for (Object o2 : arr2) {
            JSONObject obj2 = (JSONObject) o2;
            if (obj2.get("shel_nm").toString().equals(nm)) {
                String adr = obj2.get("address").toString();
                System.out.print(j + "\t");
                System.out.print(obj2.get("shel_nm") + " / ");
                System.out.print(obj2.get("shel_div_type") + " / ");
                System.out.print(obj2.get("address") + " / ");
                System.out.print(obj2.get("manage_gov_nm") + " / ");
                System.out.println(obj2.get("tel"));

                StringBuilder urlStr = new StringBuilder("ㅁㄹ");
                urlStr.append(URLEncoder.encode(adr, "UTF-8"));
                URL url1 = new URL(urlStr.toString());
                HttpURLConnection conn1 = (HttpURLConnection) url1.openConnection();

                conn1.setRequestMethod("GET");
                conn1.setRequestProperty("x-ncp-apigw-api-key-id", "ㅁㄹ");
                conn1.setRequestProperty("X-NCP-APIGW-API-KEY", "ㅁㄹ");
                conn1.setRequestProperty("Accept", "application/json");

                BufferedReader br1;
                if (conn1.getResponseCode() == 200) {
                    br1 = new BufferedReader(new InputStreamReader(conn1.getInputStream(), "UTF-8"));
                } else {
                    br1 = new BufferedReader(new InputStreamReader(conn1.getErrorStream(), "UTF-8"));
                }

                String result2 = br1.readLine();
                br1.close();
                conn1.disconnect();

                JSONParser jp1 = new JSONParser();
                JSONObject jsonObject2 = (JSONObject) jp1.parse(result2);
                JSONArray arr3 = (JSONArray) jsonObject2.get("addresses");

                for (Object o : arr3) {
                    JSONObject obj4 = (JSONObject) o;
                    String x = obj4.get("x").toString();
                    String y = obj4.get("y").toString();
                    String z = obj4.get("roadAddress").toString();

                    mapService(x, y, z);
                }
            }
        }
    }

    //맵서비스 시작
    private static void mapService(String x, String y, String z) throws IOException {
        String mapUrl = "ㅁㄹ";
        String pos = URLEncoder.encode(x + " " + y, "UTF-8");

        mapUrl += "center=" + x + "," + y;
        mapUrl += "&level=16&w=700&h=500";
        mapUrl += "&markers=type:t|size:mid|pos:" + pos + "|label:" + URLEncoder.encode(z, "UTF-8");

        URL url = new URL(mapUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        conn.setRequestProperty("x-ncp-apigw-api-key-id", "ㅁㄹ");
        conn.setRequestProperty("X-NCP-APIGW-API-KEY", "ㅁㄹ");
        conn.setRequestProperty("Accept", "application/json");


        int responseCode = conn.getResponseCode();

        if (responseCode == 200) {
            InputStream is = conn.getInputStream();
            Image image = ImageIO.read(is);
            File f = new File(z + ".jpg");
            f.createNewFile();
            ImageIO.write((RenderedImage) image, "jpg", f);
            is.close();
            System.out.println("이미지주소 : "+"C:\\Users\\it\\IdeaProjects\\javaHomework\\"+z+".jpg");
        }
        conn.disconnect();
    }
}
