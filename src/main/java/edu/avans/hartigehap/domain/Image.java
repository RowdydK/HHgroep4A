package edu.avans.hartigehap.domain;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import org.apache.xerces.impl.dv.util.Base64;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter

public abstract class Image extends DomainObject {
	
	public Image(){

	}
	
    @Column(name = "IMAGEFILENAME")
    protected String imageFileName;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	protected byte[] image;
	
    public byte[] getImage(){
    	return image;
    }
    
    public String getImageString(){
    	return "data:image/jpg;base64," + Base64.encode(getImage());
    }
    
    public byte[] imageToByteArray(){
    	byte[] result = null;
    	ByteArrayOutputStream baos = new ByteArrayOutputStream(1000);
    	try {
			BufferedImage img = ImageIO.read(new File(getClass().getResource("/../../images/" + imageFileName).getFile()));
			//BufferedImage img = ImageIO.read(new File("C:/Development/Java/Spring Tools Suite Workspace/hh/src/main/webapp/images/" + imageFileName));

			ImageIO.write(img, "jpg", baos);
			return baos.toByteArray();			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return result;
    }
    
}
