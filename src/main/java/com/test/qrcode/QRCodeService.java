package com.test.qrcode;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.glxn.qrgen.QRCode;
import net.glxn.qrgen.image.ImageType;

public class QRCodeService
{
	public static void main(String[] args)
	{
		String qrcodeContent = "https://www.baidu.com/";
		ByteArrayOutputStream out = QRCode.from(qrcodeContent).to(ImageType.PNG).stream();

		try
		{
			FileOutputStream fout = new FileOutputStream(new File("D:/Download/QR_Code.jpg"));
			fout.write(out.toByteArray());
			fout.flush();
			fout.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
