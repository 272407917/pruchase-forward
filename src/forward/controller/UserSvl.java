package forward.controller;

import forward.pojo.Users;
import forward.service.UserService;
import forward.service.serviceImp.UserServiceImp;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

/**
 * @author 瞿琮
 * @create 2020-03-09 10:21
 */
@WebServlet("/qiantai/UserSvl")
public class UserSvl extends HttpServlet {
    //提取字段
    private UserService userService=new UserServiceImp();

    //生成验证码
    private static String codeChars = "1234567890abcdefghijklmnopqrstuvwxyz";
    //得到字符的随机颜色
    private static Color getRandomColor(int minColor, int maxColor){
        Random random = new Random();
        if(minColor>255)
            minColor = 255;
        if(maxColor>255)
            maxColor = 255;
        int red = minColor + random.nextInt(maxColor - minColor);
        int green = minColor + random.nextInt(maxColor - minColor);
        int blue = minColor + random.nextInt(maxColor - minColor);
        return new Color(red,green,blue);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqType = request.getParameter("reqType");
        if (reqType.equals("reg")){
            userRegister(request,response);
        }else if (reqType.equals("checkuName")){
            checkUname(request,response);
        }else if (reqType.equals("validate")){
            genericValidateCode(request,response);
        }else if(reqType.equals("login")){
            login(request,response);
        }
    }

    //登录验证，并且验证验证码是否正确
    private void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String validateCode = request.getParameter("validateCode");

        Users user=new Users(username,password,null,null,null);

        //验证验证码，从session取出生成的验证码
        HttpSession codeSession = request.getSession();
        String validationCodeImg = String.valueOf(codeSession.getAttribute("validation_code"));
        if (validationCodeImg == null||!validationCodeImg.equalsIgnoreCase(validateCode)){//不区别大小写
            request.setAttribute("info","验证码错误！");
            request.getRequestDispatcher("login.jsp").forward(request,response);
            return;//执行完后跳出执行用户验证
        }

        //进行用户验证
        boolean result = userService.login(user);
        if (result){
            response.sendRedirect("main.jsp");
        }else {
            request.setAttribute("info","登录失败！");
            request.getRequestDispatcher("login.jsp").forward(request,response);
        }

    }

    //生成验证验证码的方法
    private void genericValidateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //获得验证码集合的长度
        int charsLength = codeChars.length();
        //下面3条是关闭客户端浏览器的缓冲区
        response.setHeader("ragma", "No-cache");
        response.setHeader("Cach-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //设置图形验证码的长宽
        int width = 90, height = 20;
        //图片对象，输入参数宽、高、背景图片类型
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
        //获得画笔对象
        Graphics g = image.getGraphics();//获得输出文字的graphics对象
        Random random = new Random();
        g.setColor(getRandomColor(180, 250));//随机的背景颜色
        g.fillRect(0, 0, width, height);//使用画笔用上面的随机颜色填充背景图片
        //设置初始字体
        g.setFont(new Font("Times New Roman",Font.ITALIC,height));
        g.setColor(getRandomColor(120, 180));//字体颜色
        StringBuilder validationCode = new StringBuilder();//验证码的可变字符串
        //验证码的随机字体
        String[] fontNames = {"Times New Roman","Book antiqua","Arial"};
        //随机生成3-5个验证码
        for (int i = 0; i < 3+random.nextInt(3); i++) {
            //随机设置当前验证码的字符的字体
            g.setFont(new Font(fontNames[random.nextInt(3)],Font.ITALIC,height));
            //随机获得当前验证码的字符
            char codeChar = codeChars.charAt(random.nextInt(charsLength));
            validationCode.append(codeChar);
            //随机设置当前验证码字符的颜色
            g.setColor(getRandomColor(10, 100));
            //在图形上输出验证码字符，x y随机生成
            g.drawString(String.valueOf(codeChar), 16*i+random.nextInt(7), height-random.nextInt(6));
        }
        //获得session对象
        HttpSession session = request.getSession();
        session.setMaxInactiveInterval(5*60);
        //将验证码保存在session对象中，key为validation_code
        session.setAttribute("validation_code", validationCode.toString());
        g.dispose();
        OutputStream os = response.getOutputStream();
        ImageIO.write(image,"JPEG",os);//以JPEG格式向客户端发送图形验证码
    }


    //检查用户名是否重复
    private void checkUname(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");

        boolean result = userService.checkuName(username);
        //将判断结果返回页面,字符串输出输出到页面都是字符串
        response.getWriter().print(result);
        response.getWriter().flush();
    }


    //注册用户
    private void userRegister(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String qqcode = request.getParameter("qqcode");

        //封装
        Users user=new Users(username,password,null,email,qqcode);

        //注册用户
        boolean result = userService.registerUser(user);

        if (result){//注册成功
            HttpSession session = request.getSession();
            session.setAttribute("user",user);
            response.sendRedirect("main.jsp");
        }else {
            request.setAttribute("info","注册失败！");
            request.getRequestDispatcher("register.jsp").forward(request,response);
        }

    }

}
