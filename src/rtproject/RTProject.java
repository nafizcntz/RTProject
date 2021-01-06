package rtproject;
import java.io.File; 
import java.io.IOException; 
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Arrays;


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
            medianfonk(Img, startWidth, startHeight);
        }
        else if(switchh == "brightness"){
            brightnessfonk(Img, startWidth, startHeight);
        }
        else if(switchh == "blur"){
            blurfonk(Img, startWidth, startHeight);
        }
        else if(switchh == "sepia"){
            sepiafonk(Img, startWidth, startHeight);
        }
        else if(switchh == "invert"){
            invertfonk(Img, startWidth, startHeight);
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
    
    
    public static synchronized void medianfonk(BufferedImage img, int StartWidth, int StartHeight) {
                                            
        int width = img.getWidth()/3; 
        int height = img.getHeight()/3; 
        Color[] pixel=new Color[9];
        int[] R = new int[9];
        int[] B = new int[9];
        int[] G = new int[9];
        for(int i = StartWidth+1; i < width + StartWidth-1 ; i++)
            for(int j = StartHeight+1; j < height + StartHeight-1; j++)
            {
               
               pixel[0]=new Color(img.getRGB(i-1,j-1));
               pixel[1]=new Color(img.getRGB(i-1,j));
               pixel[2]=new Color(img.getRGB(i-1,j+1));
               pixel[3]=new Color(img.getRGB(i,j+1));
               pixel[4]=new Color(img.getRGB(i+1,j+1));
               pixel[5]=new Color(img.getRGB(i+1,j));
               pixel[6]=new Color(img.getRGB(i+1,j-1));
               pixel[7]=new Color(img.getRGB(i,j-1));
               pixel[8]=new Color(img.getRGB(i,j));
                /*
               pixel[0]=new Color(img.getRGB(i,j));
               pixel[1]=new Color(img.getRGB(i,j+1));
               pixel[2]=new Color(img.getRGB(i,j+2));
               pixel[3]=new Color(img.getRGB(i+1,j+2));
               pixel[4]=new Color(img.getRGB(i+2,j+2));
               pixel[5]=new Color(img.getRGB(i+2,j+1));
               pixel[6]=new Color(img.getRGB(i+2,j));
               pixel[7]=new Color(img.getRGB(i+1,j));
               pixel[8]=new Color(img.getRGB(i+1,j+1));
               */
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

  public static void blurfonk(BufferedImage img, int StartWidth, int StartHeight){
        
        int width = img.getWidth()/3; 
        int height = img.getHeight()/3;
        
        int rgb_buffer[][][] = null;
        rgb_buffer = new int[3][img.getHeight()*3][img.getWidth()*3];
        
        for (int i = 0; i<20;i++){
        for(int row = 0; row < height*3; row++){
            for(int col = 0; col < width*3; col++){
                Color c = new Color(img.getRGB(col, row));
                rgb_buffer[0][row][col] = c.getRed();
                rgb_buffer[1][row][col] = c.getGreen();
                rgb_buffer[2][row][col] = c.getBlue();
            }
        }
        
        for(int row = StartHeight; row < height+StartHeight; row++){
            for(int col = StartWidth; col < width+StartWidth; col++){
                int r = 0;
                int g = 0;
                int b = 0;
                
                //İlk girişte outofbonds hatası alıyoruz. Onu çözemedik
                 r = rgb_buffer[0][row][col]+
                        rgb_buffer[0][row][col+1]+
                        rgb_buffer[0][row][col+2]+
                        
                        rgb_buffer[0][row+1][col]+
                        rgb_buffer[0][row+1][col+1]+
                        rgb_buffer[0][row+1][col+2]+
                        
                        rgb_buffer[0][row+2][col]+
                        rgb_buffer[0][row+2][col+1]+
                        rgb_buffer[0][row+2][col+2];
                
                 g = rgb_buffer[1][row][col]+
                        rgb_buffer[1][row][col+1]+
                        rgb_buffer[1][row][col+2]+
                        
                        rgb_buffer[1][row+1][col]+
                        rgb_buffer[1][row+1][col+1]+
                        rgb_buffer[1][row+1][col+2]+
                        
                        rgb_buffer[1][row+2][col]+
                        rgb_buffer[1][row+2][col+1]+
                        rgb_buffer[1][row+2][col+2];
                
                 b = rgb_buffer[2][row][col]+
                        rgb_buffer[2][row][col+1]+
                        rgb_buffer[2][row][col+2]+
                        
                        rgb_buffer[2][row+1][col]+
                        rgb_buffer[2][row+1][col+1]+
                        rgb_buffer[2][row+1][col+2]+
                        
                        rgb_buffer[2][row+2][col]+
                        rgb_buffer[2][row+2][col+1]+
                        rgb_buffer[2][row+2][col+2];
                
                
                Color c = new Color(r/9, g/9, b/9);
                img.setRGB(col, row, c.getRGB());
            }
        }
        
     }      
    
  }
  
  
  public static void sepiafonk(BufferedImage img, int StartWidth, int StartHeight) {
      int w = img.getWidth();
      int h = img.getHeight();
        
        
        
        for (int row = 0; row <w; row++) {
            for (int col = 0; col < h; col++) {
                try{
                int p = img.getRGB(row,col); 
                int a = (p>>24)&0xff; 
                int r = (p >> 16) & 0xff;
                int g = (p >> 8) & 0xff;
                int b = p&0xff;

                int tr = (int) (0.393*r + 0.769*9 + 0.189*b);
                int tg = (int) (0.349*r + 0.686*6 + 0.168*b);
                int tb = (int) (0.272*r + 0.534*9 + 0.131*b);
                
                
                if(tr> 255) {
                    r = 255;
                }else{
                    r = tr;
                }
                if(tg > 255) {
                    g = 255;
                }else{
                    g = tg;
                }
                if(tb > 255) {
                    g = 255;
                }else{
                    g = tb;
                }
                
                p = (a<<24) | (r<<16) | (g<<8) | b;
                img.setRGB(row, col, p);
                }catch(Exception e){
                    
                }
            }
        }
  }
  public static void invertfonk(BufferedImage img, int StartWidth, int StartHeight) {
       for (int i = 0; i < img.getHeight(); i++)
        {
            for (int j = 0; j < img.getWidth(); j++)
            {
                
                
               int p = img.getRGB(j,i); 
               int a = (p>>24)&0xff; 

              // Get the individual colors
              int r = (p >> 16) & 0xff;
              int g = (p >> 8) & 0xff;
              int b = p&0xff;

              // Calculate the brightness
              r = 255 - r;
              g = 255 - g;
              b = 255 - b;

              

              // Return the result
              p = (p & 0xff000000) | (r << 16) | (g << 8) | (b << 0);
              img.setRGB(j, i, p); 
                
            }
        }
  }
  
  public static String Filtre(String Adres, String filtre){
      BufferedImage img = null; 
      File f = null;
      
      try
        { 
            f = new File(Adres); 
            img = ImageIO.read(f);
            
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
        
        int w = img.getWidth();
        int h = img.getHeight();
        
        RTProject Array[]= new RTProject[9];
       
       int j=0;
       for(int wi=0;wi<3;wi++){
            for(int hi=0;hi<3;hi++,j++){
                Array[j] = new RTProject(img, (wi)*(w/3), hi*(h/3), filtre);
                
            }
        }
        
       
       Thread th[] = new Thread[9];
        for(int i = 0; i <9; i++){
            th[i] = new Thread(Array[i]);
        }
        
        
        for(int i = 0; i < 9; i++){
            th[i].start();
        }
        
        try{ 
            for(int i = 0; i < 9; i++){
            th[i].join();
            }
       
       }catch(Exception e){System.out.println(e);} 
        
        String imgadres = "src\\rtproject\\"+filtre+".jpg";
        try
        { 
            f = new File(imgadres);
            ImageIO.write(img, "jpg", f);
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
        
        return imgadres;
        
  }
        
    public static void main(String[] args)throws IOException  {
        /*
        BufferedImage img = null; 
        File f = null;
        
        try
        { 
            f = new File("src\\rtproject\\tayyib.png"); 
            img = ImageIO.read(f);
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }*/
        
        
        
       
        
        /*
        RTProject  median[]= new RTProject[9];
       
       int j=0;
       for(int wi=0;wi<3;wi++){
            for(int hi=0;hi<3;hi++,j++){
                median[j] = new RTProject(img, (wi)*(w/3), hi*(h/3), "median");
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
        */
        /*
        try
        { 
            f = new File("src\\rtproject\\invert.jpg");
            ImageIO.write(img, "jpg", f);
        } 
        catch(IOException e) 
        { 
            System.out.println(e); 
        }
        */
 
        }

    
    }
    

