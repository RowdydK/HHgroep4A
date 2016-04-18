package edu.avans.hartigehap.domain;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.apache.xerces.impl.dv.util.Base64;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class, property = "@id")
@Getter
@Setter
public class ImageReal extends Image{

    private static final long serialVersionUID = 1L;
    
    public ImageReal() {}
    
	public ImageReal(String imageFileName){
		this.imageFileName = imageFileName;
		this.image = imageToByteArray();
	}
	
}
