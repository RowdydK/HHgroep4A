package edu.avans.hartigehap.domain;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
public class ImageResized extends Image{

    private static final long serialVersionUID = 1L;
	
    
	public ImageResized(){}

	public ImageResized(String imageFileName, int theMaxWidth){
		this.imageFileName = imageFileName;
		this.image = resizeImage(imageToByteArray(),theMaxWidth);
	}
	
	public byte[] resizeImage(byte[] originalImage, int theMaxWidth){		
		ByteArrayInputStream bais = new ByteArrayInputStream(originalImage);
		try{
			//Resize
			BufferedImage bufferedImage = ImageIO.read(bais);
			double width = bufferedImage.getWidth();
			double height = bufferedImage.getHeight();
			double aspectratio = height/width;
			height = aspectratio * theMaxWidth;
			
			java.awt.Image scaled = bufferedImage.getScaledInstance(theMaxWidth, (int) height, java.awt.Image.SCALE_SMOOTH);
			//Restore as byte array
			BufferedImage buffered = new BufferedImage(theMaxWidth, (int) height, BufferedImage.TYPE_INT_RGB);
			buffered.getGraphics().drawImage(scaled, 0, 0, new Color(0,0,0), null);
			
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			
			ImageIO.write(buffered, "jpg" , baos);
			
			return baos.toByteArray();
			
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		return null;
	}
	
}
