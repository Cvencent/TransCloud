package com.vencent.transcloudtranslate.utils;

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
	 * 数组转对象
	 * @param bytes
	 * @return
	 */
	public static Object toObject (byte[] bytes) {
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
			ObjectInputStream ois = new ObjectInputStream (bis);
			obj = ois.readObject();
			ois.close();
			bis.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return obj;
	}

	/**
	 * 获得指定文件的byte数组
	 * @param filePath 文件绝对路径
	 * @return
	 */
	public static byte[] file2Byte(String filePath){
		ByteArrayOutputStream bos=null;
		BufferedInputStream in=null;
		try{
			File file=new File(filePath);
			if(!file.exists()){
				throw new FileNotFoundException("file not exists");
			}
			bos=new ByteArrayOutputStream((int)file.length());
			in=new BufferedInputStream(new FileInputStream(file));
			int buf_size=1024;
			byte[] buffer=new byte[buf_size];
			int len=0;
			while(-1 != (len=in.read(buffer,0,buf_size))){
				bos.write(buffer,0,len);
			}
			return bos.toByteArray();
		}
		catch(Exception e){
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
		finally{
			try{
				if(in!=null){
					in.close();
				}
				if(bos!=null){
					bos.close();
				}
			}
			catch(Exception e){
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

}
