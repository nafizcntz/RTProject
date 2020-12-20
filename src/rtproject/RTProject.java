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
        /*
        //int width = 963; //width of the image 
	//int height = 640; //height of the image 

	BufferedImage img = null; 

		// READ IMAGE 
	try
	{ 
            File input_file = new File("C:\\Users\\Nafiz\\Documents\\NetBeansProjects\\RTProject\\src\\rtproject\\input.jpg"); //image file path 

            //image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 

            // Reading input file 
            img = ImageIO.read(input_file); 

            System.out.println("Reading complete."); 
	} 
	catch(IOException e) 
	{ 
            System.out.println("Error: "+e); 
	} 
        */
        /*--------------------------------------------------------------------*/
      /*
        int width = img.getWidth(); 
        int height = img.getHeight(); 
  
        int p = img.getRGB(0,0); 
  
        // get alpha 
        int a = (p>>24) & 0xff; 
  
        // get red 
        int r = (p>>16) & 0xff; 
  
        // get green 
        int g = (p>>8) & 0xff; 
  
        // get blue 
        int b = p & 0xff; 
  
      
        a = 255; 
        r = 100; 
        g = 150; 
        b = 200; 
  
        //set the pixel value 
        p = (a<<24) | (r<<16) | (g<<8) | b; 
        img.setRGB(0, 0, p); 
        */
        /*--------------------------------------------------------------------*/
       /*
            // Output file path 
            File output_file = new File("C:\\Users\\Nafiz\\Documents\\NetBeansProjects\\RTProject\\src\\rtproject\\output.jpg"); 

            // Writing to file taking type and path as 
            ImageIO.write(img, "jpg", output_file); 

            System.out.println("Writing complete."); */
   
       /*
       double matrix[][] = { { 1/ 9, 1/ 9, 1/ 9}, 
                        { 1/ 9, 1/ 9, 1/ 9}, 
                        { 1/ 9, 1/ 9, 1/ 9} }; 
       
       //int filtre[][] = matrix[][] / 9;
       //filtre = double(filtre);

       Output = zeros(size(img));

        for (i = 1;size(img, 1)-2)
            for j = 1:size(img, 1)-2
                Temp = img(i : i+2, j : j+2).*filtre(3,3,:);
                Output(i, j) = sum(Temp(:));
       
        for (int y = StartHeight; y < height+StartHeight; y++) 
        { 
            for (int x = StartWidth; x < width+StartWidth; x++) 
            { 
                 
            } 
        } */
       /*
       int maxWidth = img.getWidth(); 
       int maxHeight = img.getHeight();
       int pictureFile[][] = new int [maxHeight][maxWidth];
        for( int i = 0; i < maxHeight; i++ ){
            for( int j = 0; j < maxWidth; j++ ){
                pictureFile[i][j] = img.getRGB( j, i );
            }
        }
        
        int output [][] = new int [maxHeight][maxWidth];

        //Apply Mean Filter
        for (int v=1; v<maxHeight; v++) {
            for (int u=1; u<maxWidth; u++) {
                //compute filter result for position (u,v)

                int sum = 0;
                for (int j=-1; j<=1; j++) {
                    for (int i=-1; i<=1; i++) {
                        if((u+(j)>=0 && v+(i)>=0 && u+(j)<maxWidth && v+(i)<maxHeight)){
                        int pixel=pictureFile[u+i][v+j];
                        int rr=(pixel&0x00ff0000)>>16, rg=(pixel&0x0000ff00)>>8, rb=pixel&0x000000ff;
                        sumr+=rr;
                        sumg+=rg;
                        sumb+=rb;
                        }
                    }
                }

                int q = (int) (sum /9);
                output[v][u] = q;
            }
        }

        //Turn the 2D array back into an image
        BufferedImage theImage = new BufferedImage(
            maxHeight, 
            maxWidth, 
            BufferedImage.TYPE_INT_RGB);
        int value;
        for(int y = 1; y<maxHeight; y++){
            for(int x = 1; x<maxWidth; x++){
                value = output[y][x] ;
                theImage.setRGB(y, x, value);
            }
        }
       */
        
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
