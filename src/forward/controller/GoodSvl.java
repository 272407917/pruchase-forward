package forward.controller;

import forward.pojo.Goods;
import forward.service.GoodService;
import forward.service.serviceImp.GoodServiceImp;

import javax.servlet.GenericServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * @author 瞿琮
 * @create 2020-03-11 10:14
 */
@WebServlet("/qiantai/goodSvl")
public class GoodSvl extends HttpServlet {

    private GoodService goodService= new GoodServiceImp();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String reqType = request.getParameter("reqType");
        if (reqType.equals("main")){
            int pageNow = Integer.valueOf(request.getParameter("pageNow"));
            openMain(request,response,pageNow);
        }else if (reqType.equals("downImg")){
            downImg(request,response);
        }
    }

    //向用户端传输商品图片，因为图片在安全目录upload下所以不能直接访问，只能通过控制器传输图片
    private void downImg(HttpServletRequest request, HttpServletResponse response) {
        String filename = request.getParameter("filename");
        String path=request.getServletContext().getRealPath("/WEB-INF/upload/"+filename);

        FileInputStream fis=null;
        ServletOutputStream os = null;
        try {
            int len=0;
            byte[] buff=new byte[1024];

            fis=new FileInputStream(path);
            os=response.getOutputStream();
            while ((len=fis.read(buff))!=-1){
                os.write(buff,0,len);
            }
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }


    //请求进入主页面获取右下角商品类别信息，并转发到首页面
    private void openMain(HttpServletRequest request, HttpServletResponse response,int pageNow) throws ServletException, IOException {
        int pageSize=20;
        if (pageNow<0){
            pageNow=1;
        }
        String goodType = request.getParameter("goodType");
        List<String> allGoodType = goodService.findAllGoodType();
        List<Goods> goodsByType=null;
        if (allGoodType.size()>0){
            if (goodType==null||goodType.isEmpty()){//页面默认显示第一个类型的商品
                goodType=allGoodType.get(0);
            }
            goodsByType = goodService.findGoodsByType(goodType, pageNow, pageSize);
        }

        request.setAttribute("goodTypeList",allGoodType);
        request.setAttribute("goodsByTypeList",goodsByType);
        request.setAttribute("pageNow",pageNow);
        request.getRequestDispatcher("main.jsp").forward(request,response);
    }
}
