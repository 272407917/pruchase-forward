package forward.controller;

import forward.pojo.CarBean;
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
import javax.servlet.http.HttpSession;
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
        }else if (reqType.equals("addCar")){
            addCar(request,response);
        }else if (reqType.equals("delGood")){
            delGood(request,response);
        }else if (reqType.equals("clearCar")){
            clearCar(request,response);
        }else if (reqType.equals("modCar")){
            modCar(request,response);
        }else if (reqType.equals("flow")){
            openFlow(request,response);
        }else if (reqType.equals("calculator")){
            calculator(request,response);
        }
    }

    //结算中心
    private void calculator(HttpServletRequest request, HttpServletResponse response) {
        HttpSession carSession = request.getSession();
        //得到carSession中存放的购物车对象
        CarBean carBean=(CarBean) carSession.getAttribute("car");
        double blance=carBean.getBlance();
        try {
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().print("<script>alert('支付成功，支付金额：￥"+blance+"')</script>");
            response.getWriter().print(
            "<script>"+
            "if (window.confirm('是否返回主页面')) {"+
                "document.location.href='goodSvl?reqType=main&pageNow=1'"+
            "}"+
	        "</script>"
            );
            response.getWriter().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查看购物车连接
    private void openFlow(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession carSession = request.getSession();
        //得到carSession中存放的购物车对象
        CarBean carBean=(CarBean) carSession.getAttribute("car");
        //调用购物车商品信息展示方法
        if (carBean!=null){
            genericCarData(request,response,carBean,carSession);
        }else {
            request.getRequestDispatcher("flow.jsp").forward(request,response);
        }

    }

    //更新购物车
    private void modCar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //得到购物车中商品id和数量amount，有多个商品所以是数组
        String[] goodids = request.getParameterValues("goodids");
        String[] amounts = request.getParameterValues("amounts");
        //将字符串数组里的数据转换成整数型
        Integer[] goodids_int=new Integer[goodids.length];
        Integer[] amounts_int=new Integer[amounts.length];
        for (int i = 0; i < goodids.length; i++) {
            goodids_int[i]=Integer.valueOf(goodids[i]);
            amounts_int[i]=Integer.valueOf(amounts[i]);
        }
        HttpSession carSession = request.getSession();
        //得到carSession中存放的购物车对象
        CarBean carBean=(CarBean) carSession.getAttribute("car");
        //更新购物车
        carBean.modGood(goodids_int,amounts_int);
        //调用购物车商品信息展示方法
        genericCarData(request,response,carBean,carSession);
    }

    //清空购物车
    private void clearCar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession carSession = request.getSession();
        //得到carSession中存放的购物车对象
        CarBean carBean=(CarBean) carSession.getAttribute("car");
        carBean.clearCar();
        //调用购物车商品信息展示方法
        genericCarData(request,response,carBean,carSession);
    }

    //删除商品
    private void delGood(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String goodid = request.getParameter("goodid");
        Integer id=Integer.valueOf(goodid);
        HttpSession carSession = request.getSession();
        //得到carSession中存放的购物车对象
        CarBean carBean=(CarBean) carSession.getAttribute("car");
        carBean.removeGood(id);
        //调用购物车商品信息展示方法
        genericCarData(request,response,carBean,carSession);
    }


    //添加商品进入购物车
    private void addCar(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Integer goodid = Integer.valueOf(request.getParameter("goodid"));
        HttpSession carSession = request.getSession();
        //从carSession中取出购物车对象，若为null说明还没有添加购物车
        CarBean carBean=(CarBean) carSession.getAttribute("car");
        if (carBean==null){
            carBean=new CarBean();//创建购物车对象
        }
        carBean.addGood(goodid);
        genericCarData(request,response,carBean,carSession);
    }

    //通过购物车car中的id查询出商品信息，并在页面中展示出来
    private void genericCarData(HttpServletRequest request, HttpServletResponse response,CarBean carBean,HttpSession carSession) throws ServletException, IOException {
        //将得到的购物车对象放入carSession中存放
        carSession.setAttribute("car",carBean);
        List<Goods> carGoodData = goodService.findCarData(carBean);
        request.setAttribute("carGoodData",carGoodData);
        request.getRequestDispatcher("flow.jsp").forward(request,response);
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
        HttpSession carSession = request.getSession();
        //得到carSession中存放的购物车对象
        CarBean carBean=(CarBean) carSession.getAttribute("car");
        int amounts=0;
        double blance=0;
        if (carBean!=null){
            amounts=carBean.getAllAmounts();
            blance=carBean.getBlance();
        }
        request.setAttribute("allAmount",amounts);
        request.setAttribute("Blance",blance);

        request.setAttribute("goodTypeList",allGoodType);
        request.setAttribute("goodsByTypeList",goodsByType);
        request.setAttribute("pageNow",pageNow);
        request.getRequestDispatcher("main.jsp").forward(request,response);
    }
}
