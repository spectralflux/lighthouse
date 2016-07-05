package com.spectralflux.lighthouse.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

public class RenderComponent implements Component {
	
	private Texture img;
	private float originX;
	private float originY;
	private Color color;
	
	public RenderComponent(Texture img) {
		setImg(img);
		color = new Color(1,1,1,1);
	}
	
	public RenderComponent(Texture img, Color color) {
		setImg(img);
		this.color = color;
	}
	
	private void setImg(Texture img) {
		this.img = img;
		originX = img.getWidth()/2;
		originY = img.getHeight()/2;
	}
	
	public Texture img() {
		return img;
	}
	
	public void setColor(Color newColor) {
		color = newColor;
	}
	
	public Color color() {
		return color;
	}
	
	public float originX() {
		return originX;
	}
	
	public float originY() {
		return originY;
	}
	
	public float width() {
		return img.getWidth();
	}
	
	public float height() {
		return img.getHeight();
	}
	
	public float scaleX() {
		return 1;
	}
	
	public float scaleY() {
		return 1;
	}
	
	public int srcX() {
		return 0;
	}
	
	public int srcY() {
		return 0;
	}
	
	public int srcWidth() {
		return (int) width();
	}
	
	public int srcHeight() {
		return (int) height();
	}
	
	public boolean flipX() {
		return false;
	}
	
	public boolean flipY() {
		return false;
	}

}
