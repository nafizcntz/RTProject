package rtproject;
import java.io.File; 
import java.io.IOException; 
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Arrays;
import javax.imageio.*;


public class RTProject extends Thread {
    BufferedImage Img;
    int startWidth;
    int startHeight;
    
    public RTProject(BufferedImage img, int StartWidth, int StartHeight) {
        this.Img = img;
        this.startWidth = StartWidth;
        this.startHeight = StartHeight;
    }
    public void run1(){
        grayfonk(Img, startWidth, startHeight);
    }
    public void run2(){
        medianfonk(Img, startWidth, startHeight);
    }
    public void run3(){
        brightnessfonk(Img, startWidth, startHeight);
    }

    public static synchronized void  grayfonk(BufferedImage img, int StartWidth, int StartHeight) {

        // get image's width and height 
        int width = img.getWidth()/3; 
        int height = img.getHeight()/3; 
  
        // convert to greyscale 
        for (int y = StartHeight; y < height+StartHeight; y++) 
        { 
            for (int x = StartWidth; x < width+StartWidth; x++) 
            { 
                // Here (x,y)denotes the coordinate of image  
                // for modifying the pixel value. 
                int p = img.getRGB(x,y); 
                int a = (p>>24)&0xff; 
                int r = (p>>16)&0xff; 
                int g = (p>>8)&0xff; 
                int b = p&0xff; 
  
                // calculate average 
                int avg = (r+g+b)/3; 
  
                // replace RGB value with avg 
                p = (a<<24) | (avg<<16) | (avg<<8) | avg; 
  
                img.setRGB(x, y, p); 
            } 
        } 
    }
    
    
    public static synchronized void medianfonk(BufferedImage img, int StartWidth, int StartHeight) {
                                    //Input Photo File
                                    
        int width = img.getWidth()/3; 
        int height = img.getHeight()/3; 
        Color[] pixel=new Color[9];
        int[] R=new int[9];
        int[] B=new int[9];
        int[] G=new int[9];
        for(int i = StartHeight; i < height+StartHeight; i++)
            for(int j = StartWidth; j < width+StartWidth; j++)
            {
               pixel[0]=new Color(img.getRGB(i,j));
               pixel[1]=new Color(img.getRGB(i,j+1));
               pixel[2]=new Color(img.getRGB(i,j+2));
               pixel[3]=new Color(img.getRGB(i+1,j+2));
               pixel[4]=new Color(img.getRGB(i+2,j+2));
               pixel[5]=new Color(img.getRGB(i+2,j+1));
               pixel[6]=new Color(img.getRGB(i+2,j));
               pixel[7]=new Color(img.getRGB(i+1,j));
               pixel[8]=new Color(img.getRGB(i+1,j+1));
               for(int k=0;k<9;k++){
                   R[k]=pixel[k].getRed();
                   B[k]=pixel[k].getBlue();
                   G[k]=pixel[k].getGreen();
               }
               Arrays.sort(R);
               Arrays.sort(G);
               Arrays.sort(B);
               img.setRGB(i,j,new Color(R[4],B[4],G[4]).getRGB());
            }
       }
    
    
  public static synchronized void brightnessfonk(BufferedImage img, int StartWidth, int StartHeight) {
      int brightness = 300;      
      int width = img.getWidth()/3; 
      int height = img.getHeight()/3;
      
      for (int y = StartHeight; y < height+StartHeight; y++) 
        { 
            for (int x = StartWidth; x < width+StartWidth; x++) 
            { 
               int p = img.getRGB(x,y); 
               int a = (p>>24)&0xff; 

              // Get the individual colors
              int r = (p >> 16) & 0xff;
              int g = (p >> 8) & 0xff;
              int b = p&0xff;

              // Calculate the brightness
              r += (brightness * r) / 100;
              g += (brightness * g) / 100;
              b += (brightness * b) / 100;

              // Check the boundaries
              r = Math.min(Math.max(0, r), 255);
              g = Math.min(Math.max(0, g), 255);
              b = Math.min(Math.max(0, b), 255);

              // Return the result
              p = (p & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
              img.setRGB(x, y, p); 
    }
  }
}
    
    
    public static void main(String[] args)throws IOException  {
        
        BufferedImage img = null; 
        File f = null; 
  
        // read image 
        try
        { 
            f = new File("C:\\Users\\Nafiz\\Documents\\NetBeansProjects\\RTProject\\src\\rtproject\\input.jpg"); 
            img = ImageIO.read(f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 
       
       
       RTProject a = new RTProject(img, 0, 0);
       RTProject b = new RTProject(img, 300, 0);
       RTProject c = new RTProject(img, 0, 600);
       
       a.run1();
       b.run2();
       c.run3();
       
        try
        { 
            f = new File("C:\\Users\\Nafiz\\Documents\\NetBeansProjects\\RTProject\\src\\rtproject\\output.jpg"); 
            ImageIO.write(img, "jpg", f); 
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 
   
    
        
  
      
       
       
       
       
       
       
       
       
       
       
       
       
	
    }
    
}
