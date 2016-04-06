package edu.avans.hartigehap.domain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
public class ImageProxy extends Image{

    private static final long serialVersionUID = 1L;
    
	public ImageProxy(){

	}

	public ImageProxy(String imageFileName){
		this.imageFileName = imageFileName;
		getImage();
	}

    @OneToOne(cascade = javax.persistence.CascadeType.ALL)
	private ImageReal realImage;
	
    @OneToOne(cascade = javax.persistence.CascadeType.ALL)
	private ImageResized resizedImage;
	
	@Override
	public byte[] getImage(){
		int theMaxWidth = 150;
		if (realImage == null){
			realImage = new ImageReal(imageFileName);
		}
	    if (getMaxWidth(realImage.getImage(), theMaxWidth)){
	    	return realImage.getImage();
	    }
	    else{
	    	if (resizedImage == null){
	    		resizedImage = new ImageResized(imageFileName, theMaxWidth);
	    	}
	    	return resizedImage.getImage();
	    }
	}
	
	public boolean getMaxWidth(byte[] theImage, int theMaxWidth){
		ByteArrayInputStream in = new ByteArrayInputStream(theImage);
		try{
			BufferedImage img = ImageIO.read(in);
			if (img.getWidth() < theMaxWidth ){
				return true;
			}
			else{
				return false;
			}
		}
		catch(Exception e){
			System.out.println("ImageProxy class - " + e);
		}
		return false;
	}
	
//	public boolean getAspectRatio(byte[] theImage){
//		ByteArrayInputStream in = new ByteArrayInputStream(theImage);
//		try{
//			BufferedImage img = ImageIO.read(in);
//			if ((img.getHeight() % img.getWidth()) == 0 ){
//				return true;
//			}
//			else{
//				return false;
//			}
//		}
//		catch(Exception e){
//			System.out.println("ImageProxy class - " + e);
//		}
//		return false;
//	}
	
}
