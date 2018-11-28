package a.b.c01;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;


public class ServletDemo01 extends HttpServlet {
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		
		//System.out.println(request.getParameter("username"));//null
		//System.out.println(request.getParameter("password"));//null
		//System.out.println(request.getParameter("userhead"));//null
		//request.getParameter(key);  获取到的是请求体部分为键值对或者说浏览器路劲后键值对的数据
		//http://localhost:8081/TestUpload/ServletDemo01?username=ddd
		
		//通过request获取到一个输入流对象
		//InputStream is=request.getInputStream();
		//打印输入流对象中的内容
		
		//int i=is.read();
		//while(i!=-1){
		//char c=(char)i;
		//System.out.print(c);
		//i=is.read();
		//}
		
		//MAP目的:携带数据{username<==>tom,password<==>1234,userhead<==>/images/11.bmp}
		Map<String,String> map=new HashMap<String,String>();
		//目的:携带数据,向sercie,dao传递
		User user=new User();
		try {
			//以下三行代码功能: 通过request.getInputStream();获取到请求体的全部内容
			//进行解析,将每对分割线中的内容封装在了FileItem对象中
			DiskFileItemFactory fac=new DiskFileItemFactory();
			ServletFileUpload upload=new ServletFileUpload(fac);
			List<FileItem> list=upload.parseRequest(request);
			//4_遍历集合
			for (FileItem item : list) {
				if(item.isFormField()){
					//5_如果当前的FileItem对象是普通项
					//将普通项上name属性的值作为键,将获取到的内容作为值,放入MAP中
					// {username<==>tom,password<==>1234}
					//System.out.println(item.getFieldName()+"    ...普通项"); //username   password
					//System.out.println(item.getName()+"    ...普通项"); //null    null
					//System.out.println(item.getString("utf-8")+"    ...普通项"); //piter  2222222
					map.put(item.getFieldName(), item.getString("utf-8"));
					
					
				}else{
					//6_如果当前的FileItem对象是上传项
					//通过FileItem获取到输入流对象,通过输入流可以获取到图片二进制数据
					//在服务端创建一个空文件(后缀必须和上传到服务端的文件名后缀一致)
					//建立和空文件对应的输出流
					//将输入流中的数据刷到输出流中
					//释放资源
					//向map中存入一个键值对的数据 userhead<===> /image/11.bmp
					// {username<==>tom,password<==>1234,userhead<===>image/11.bmp}
					//System.out.println(item.getFieldName());  //userhead
					//System.out.println(item.getName());    //11.bmp 
					//System.out.println(item.getString());  //图片的二进制数据,不推荐大家使用此API获取图片数据
					//item.getInputStream();//推荐通过此API获取图片数据
					
					//通过FileItem获取到输入流对象,通过输入流可以获取到图片二进制数据
					InputStream is=item.getInputStream();
					//D:\tomcat\tomcat71_sz07\webapps\TestUpload\images\img
					String realPath=getServletContext().getRealPath("/images/img/");
					//在服务端创建一个空文件(后缀必须和上传到服务端的文件名后缀一致)
					File file=new File(realPath,item.getName());
					if(!file.exists()){
						file.createNewFile();
					}
					//建立和空文件对应的输出流
					OutputStream os=new FileOutputStream(file);
					//将输入流中的数据刷到输出流中
					//释放资源
					IOUtils.copy(is, os);
					IOUtils.closeQuietly(is);
					IOUtils.closeQuietly(os);
					//向map中存入一个键值对的数据 userhead<===> /image/11.bmp
					map.put("userhead", "/images/img/"+item.getName());
				}
			}
			//7_利用BeanUtils将MAP中的数据填充到user对象上
			BeanUtils.populate(user, map);
			//8_调用servcie_dao将user上携带的数据存入数据仓库,重定向到查询全部商品信息路径
			System.out.println("保存用户数据");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
