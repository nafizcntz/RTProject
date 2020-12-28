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


public class RTProject implements Runnable {
    String switchh;
    BufferedImage Img;
    int startWidth;
    int startHeight;
    String foto_yolu;
    
    public RTProject(BufferedImage img, int StartWidth, int StartHeight, String Switchh) {
        this.switchh = Switchh;
        this.Img = img;
        this.startWidth = StartWidth;
        this.startHeight = StartHeight;
    }
    public void run(){
        if (switchh == "gray"){
            grayfonk(Img, startWidth, startHeight);
        }
        else if(switchh == "median"){
            //medianfonk(Img, startWidth, startHeight);
        }
        else if(switchh == "brightness"){
            brightnessfonk(Img, startWidth, startHeight);
        }
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
    
    /*
    public static synchronized void medianfonk(BufferedImage img, int StartWidth, int StartHeight) {
                                            
        int width = img.getWidth(); 
        int height = img.getHeight(); 
        Color[] pixel=new Color[9];
        int[] R=new int[9];
        int[] B=new int[9];
        int[] G=new int[9];
        for(int i = StartHeight; i < height; i++)
            for(int j = StartWidth; j < width; j++)
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
    */
  
  public static synchronized void brightnessfonk(BufferedImage img, int StartWidth, int StartHeight) {
       
      int brightness = 500;      
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
        
        BufferedImage img1 = null;
        File f1 = null;
        
        BufferedImage img2 = null;
        File f2 = null;
        
        try
        { 
            f = new File("src\\rtproject\\input.jpg"); 
            img = ImageIO.read(f);
            img1 = ImageIO.read(f);
            img2 = ImageIO.read(f);
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
        
        int w = img.getWidth();
        int h = img.getHeight();
              
       /*-----------------------------GRAY------------------------------ */
       
       RTProject  gray[]= new RTProject[9];
       
       int j=0;
       for(int wi=0;wi<3;wi++){
            for(int hi=0;hi<3;hi++,j++){
                gray[j] = new RTProject(img, (wi)*(w/3), hi*(h/3), "gray");
            }
        }
       
        Thread th[] = new Thread[9];
        for(int i = 0; i <9; i++){
        th[i] = new Thread(gray[i]);
        }
        
        for(int i =0; i < 9; i++){
        th[i].start();
        }
        
        try{ 
            for(int i = 0; i < 9; i++){
            th[i].join();
            }
       
       }catch(Exception e){System.out.println(e);} 
        
        
        try
        { 
            f = new File("src\\rtproject\\gray.jpg");
            ImageIO.write(img, "jpg", f);
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
        
       /*------------------------------------------------------------------------ */ 
       
       
       /*-----------------------------MEDİAN------------------------------ */
       //RTProject median = new RTProject(foto_yolu,img, 1, 1);
       /*
       RTProject  median[]= new RTProject[9];
       
       j=0;
       for(int wi=0;wi<3;wi++){
            for(int hi=0;hi<3;hi++,j++){
                median[j] = new RTProject(img2, (wi)*(w/3), hi*(h/3), "median");
            }
        }
       
        Thread th2[] = new Thread[9];
        for(int i = 0; i <9; i++){
        th2[i] = new Thread(median[i]);
        }
        
        for(int i =0; i < 9; i++){
        th2[i].start();
        }
        
        try{ 
            for(int i = 0; i < 9; i++){
            th2[i].join();
            }
       
       }catch(Exception e){System.out.println(e);} 
        
        
        try
        { 
            f2 = new File("src\\rtproject\\median.jpg");
            ImageIO.write(img2, "jpg", f2);
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
       */
       /*------------------------------------------------------------------------*/
       
  
       /*-----------------------------BRIGHTNESS------------------------------ */
       
       
       RTProject  brightness[]= new RTProject[9];
       
       j=0;
       for(int wi=0;wi<3;wi++){
            for(int hi=0;hi<3;hi++,j++){
                brightness[j] = new RTProject(img1, (wi)*(w/3), hi*(h/3), "brightness");
            }
        }
       
       
       Thread th1[] = new Thread[9];
       for(int i = 0; i <9; i++){
            th1[i] = new Thread(brightness[i]);
       }
 
       
       for(int i =0; i < 9; i++){
            th1[i].start();
       }
       
       try{      
            for(int i = 0; i < 9; i++){
                th1[i].join();
            }
       }catch(Exception e){System.out.println(e);}  

       /*
       
       RTProject brightness = new RTProject(img1, 0, 0, "brightness");
       
       Thread th1 = new Thread(brightness);
       th1.start();
       try{
            th1.join();
            
       }catch(Exception e){System.out.println(e);}
       */
       try
        { 
            f1 = new File("src\\rtproject\\brightness.jpg");
            ImageIO.write(img1, "jpg", f1);
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        } 
    /*------------------------------------------------------------------------ */
    }
    
}
