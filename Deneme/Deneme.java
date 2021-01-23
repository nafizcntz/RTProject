/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rtproject;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Nafiz
 */
public class Deneme {
    
      public static void blurfonk(BufferedImage img, int StartWidth, int StartHeight){
        
        int width = img.getWidth()/3; 
        int height = img.getHeight()/3;
      
        int rgb_buffer[][][] = null;
        rgb_buffer = new int[3][height*3+1][width*3+1];
        
        for (int i = 0; i<20;i++){
        for(int row = 0; row < height*3; row++){
            for(int col = 0; col < width*3; col++){
                Color c = new Color(img.getRGB(col, row));
                rgb_buffer[0][row][col] = c.getRed();
                rgb_buffer[1][row][col] = c.getGreen();
                rgb_buffer[2][row][col] = c.getBlue();
            }
        }
        
        for(int row = StartHeight+2; row < height+StartHeight-2; row++){
            for(int col = StartWidth+2; col < width+StartWidth-2; col++){
                int r = 0;
                int g = 0;
                int b = 0;
                
                
                 r = rgb_buffer[0][row-2][col-2]+
                        rgb_buffer[0][row-2][col-1]+
                        rgb_buffer[0][row-2][col]+
                        
                        rgb_buffer[0][row-1][col-2]+
                        rgb_buffer[0][row-1][col-1]+
                        rgb_buffer[0][row-1][col+2]+
                        
                        rgb_buffer[0][row][col-2]+
                        rgb_buffer[0][row][col-1]+
                        rgb_buffer[0][row][col];
                
                 g = rgb_buffer[1][row-2][col-2]+
                        rgb_buffer[1][row-2][col-1]+
                        rgb_buffer[1][row-2][col]+
                        
                        rgb_buffer[1][row-1][col-2]+
                        rgb_buffer[1][row-1][col-1]+
                        rgb_buffer[1][row-1][col+2]+
                        
                        rgb_buffer[1][row][col-2]+
                        rgb_buffer[1][row][col-1]+
                        rgb_buffer[1][row][col];
                
                 b = rgb_buffer[2][row-2][col-2]+
                        rgb_buffer[2][row-2][col-1]+
                        rgb_buffer[2][row-2][col]+
                        
                        rgb_buffer[2][row-1][col-2]+
                        rgb_buffer[2][row-1][col-1]+
                        rgb_buffer[2][row-1][col+2]+
                        
                        rgb_buffer[2][row][col-2]+
                        rgb_buffer[2][row][col-1]+
                        rgb_buffer[2][row][col];
                 
                
                Color c = new Color(r/9, g/9, b/9);
                img.setRGB(col, row, c.getRGB());
            }
        }
        
     }      
    
  }
    public static void main(String[] args)throws IOException  {
        
        BufferedImage img = null; 
        File f = null;
         
        
        try
        { 
            f = new File("src\\rtproject\\input.jpg"); 
            img = ImageIO.read(f);
            
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
        
        int w = img.getWidth();
        int h = img.getHeight();
        
        
        blurfonk(img, 0,0);
        blurfonk(img, 600,600);
        blurfonk(img, 0,300);
        
        
        
        
        
         try
        { 
            f = new File("src\\rtproject\\Deneme\\deneme2.jpg");
            ImageIO.write(img, "jpg", f);
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
        
        /*
        int[][] MOTION_FILTR_5 =   { {  1,0,0,0,0  },
                                                 {  0,1,0,0,0  },
                                                 {  0,0,1,0,0  },
                                                 {  0,0,0,1,0  },
                                                 {  0,0,0,0,1  } };
        
        int pixel[];
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int n=0;
                for(int k =0 ; k<MOTION_FILTR_5.length;k++){
                    for(int l=0;l<MOTION_FILTR_5[k].length;l++){
                        int imgX = (i-MOTION_FILTR_5.length/2+k+img.getWidth())%img.getWidth();
                        int imgY = (j-MOTION_FILTR_5[k].length/2+l+img.getHeight())%img.getHeight();
                        pixel = img.getRaster().getPixel(imgX,imgY, new int[3]);
                        //get the new value
                        n += pixel[0]*MOTION_FILTR_5[k][l];
 
                         
                    }
                }
                n = n/5;
                if(n>255)
                    n =255;
                else if(n<0)
                    n=0;
                Color color = new Color(n, n, n);
 
                int rgb = color.getRGB();
 
                img.setRGB(i, j, rgb);
 
            
 
        }
    }*/
        
        
        
        /*---------------------------------------------------------------------*/
        /*
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
        
        
        
        int[][] MEEAN_FILTR_3 = {{  1,1,1  },
                             {  1,1,1  },
                             {  1,1,1  }};
    
        int pixel[];
        //looping trough each pixel of the image
        for (int i = 0; i < img.getWidth(); i++) {
            for (int j = 0; j < img.getHeight(); j++) {
                int n=0;
                //looping into filtering matrix
                for(int k =0 ; k<MEEAN_FILTR_3.length;k++){
                    for(int l=0;l<MEEAN_FILTR_3[k].length;l++){
                        //getting the corresponding neighboring pixel
                        // we use modulus of the width or height of the image so that pixels outside the image will be wrapped around
                        int imgX = (i-MEEAN_FILTR_3.length/2+k+img.getWidth())%img.getWidth();
                        int imgY = (j-MEEAN_FILTR_3[k].length/2+l+img.getHeight())%img.getHeight();
                        pixel = img.getRaster().getPixel(imgX,imgY, new int[3]);
                        //get the new value
                        n += pixel[0]*MEEAN_FILTR_3[k][l];
 
                         
                    }
                }
                //normalize the value
                n = n/9;
                if(n>255)
                    n =255;
                else if(n<0)
                    n=0;
                //set the new pixel value
                Color color = new Color(n, n, n);
 
                int rgb = color.getRGB();
 
                img.setRGB(i, j, rgb);
 
            }
        }
        
        try
        { 
            f = new File("src\\rtproject\\deneme.jpg");
            ImageIO.write(img, "jpg", f);
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
        */
    }
}
    

