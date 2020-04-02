package com.vencent.transclouddata.utils;

import org.springframework.util.ClassUtils;

import java.io.*;
import java.time.LocalDateTime;

/**
 * 文件操作工具类
 */
public class FileUtil {
	/**
	 * 读取文件内容为二进制数组
	 * 
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] read(String filePath) throws IOException {

		InputStream in = new FileInputStream(filePath);
		byte[] data = inputStream2ByteArray(in);
		in.close();

		return data;
	}

	/**
	 * 流转二进制数组
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	static byte[] inputStream2ByteArray(InputStream in) throws IOException {

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024 * 4];
		int n = 0;
		while ((n = in.read(buffer)) != -1) {
			out.write(buffer, 0, n);
		}
		return out.toByteArray();
	}

	/**
	 * 保存文件
	 * 
	 * @param filePath
	 * @param fileName
	 * @param content
	 */
	public static void save(String filePath, String fileName, byte[] content) {
		try {
			File filedir = new File(filePath);
			if (!filedir.exists()) {
				filedir.mkdirs();
			}
			File file = new File(filedir, fileName);
			OutputStream os = new FileOutputStream(file);
			os.write(content, 0, content.length);
			os.flush();
			os.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 以当前时间为目录获取文件路径
	 * @return String
	 */
	public static String GetFilePath(){
		LocalDateTime localDateTime = LocalDateTime.now();
		StringBuilder sb = new StringBuilder(ClassUtils.getDefaultClassLoader().getResource("").getPath());
		String path =  sb.toString().replace("/",File.separator);
		String filePath  = path +localDateTime.toString().replace(".","").replace(":","");
		return filePath;
	}


	/**
	 * 递归删除目录下的所有文件及子目录下所有文件
	 * @param dir 将要删除的文件目录
	 * @return
	 */
	public  static boolean deleteFile(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();


			for (int i=0; i<children.length; i++) {
				boolean success = deleteFile(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}
	/**
	 * 对象转数组
	 * @param obj
	 * @return
	 */
	public static byte[] toByteArray (Object obj) {
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos);
			oos.writeObject(obj);
			oos.flush();
			bytes = bos.toByteArray ();
			oos.close();
			bos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return bytes;
	}
	/**
	 * 根据byte数组，生成文件
	 * @param bytes 文件数组
	 * @param filePath 文件存放路径
	 * @param fileName 文件名称
	 */
	public static void byte2File(byte[] bytes,String filePath,String fileName){
		BufferedOutputStream bos=null;
		FileOutputStream fos=null;
		File file=null;
		try{
			File dir=new File(filePath);
			if(!dir.exists() && !dir.isDirectory()){//判断文件目录是否存在
				dir.mkdirs();
			}
			file=new File(filePath+File.separator+fileName);
			fos=new FileOutputStream(file);
			bos=new BufferedOutputStream(fos);
			bos.write(bytes);
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		finally{
			try{
				if(bos != null){
					bos.close();
				}
				if(fos != null){
					fos.close();
				}
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}


}
