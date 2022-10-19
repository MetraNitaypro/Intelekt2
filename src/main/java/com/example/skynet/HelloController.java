package com.example.skynet;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.RenderedImage;
import java.io.*;
import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

public class HelloController {
    private GraphicsContext gc;
    private boolean tag2 = false;
    private boolean tag1 = false;
    private double x1, y1;

    @FXML
    private Canvas cn;
    @FXML
    private Button btn2;

    static private double[][][] W;

    @FXML
    protected void onHelloButtonClick() {
        Scanner scan;
        File file = new File("C:\\Users\\artem\\Desktop\\Skynet\\Result.txt");
        W = new double[2][32][32];
        try {
            scan = new Scanner(file);
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 32; j++) {
                    for (int k = 0; k < 32; k++) {
                        if (scan.hasNextDouble()) {
                            W[i][j][k] = scan.nextDouble();
                        }
                    }
                }
            }

        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        System.out.println("onMouseClicked");
        var gc = cn.getGraphicsContext2D();
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
    }


    public void onMouseMoved(MouseEvent mouseEvent) {

    }

    public void onMousePressed(MouseEvent mouseEvent) {
        tag1 = true;
        x1 = mouseEvent.getX();
        y1 = mouseEvent.getY();
        System.out.println("onMousePressed");
    }

    //onMouseRelesed
    public void onMous(MouseEvent mouseEvent) {
        var gc = cn.getGraphicsContext2D();
        tag1 = false;
        System.out.println("onMouseRelesed");
        File file = new File("MyFile.png");
        try {
            WritableImage writableImage = new WritableImage(32, 32);
            gc.getCanvas().snapshot(null, writableImage);
            RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
            ImageIO.write(renderedImage, "png", file);
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Error!");
        }
        BufferedImage bufferedImageInput = null;
        try {
            bufferedImageInput = ImageIO.read(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int[][] pixels = convertTo2DWithoutUsingGetRGB(bufferedImageInput);
        char fef = (char) result(pixels);
        fef += 'A';
        String fef1 = new String();
        fef1 += fef;
        btn2.setText(fef1);
    }

    public void onMouseDragged(MouseEvent mouseEvent) {
        System.out.println("onMouseDraged");
        var gc = cn.getGraphicsContext2D();
        gc.beginPath();
        gc.moveTo(x1, y1);
        System.out.println("onMouseMoved");
        if (tag1) {
            if (!tag2) gc.moveTo(x1, y1);
            gc.lineTo(mouseEvent.getX(), mouseEvent.getY());
            x1 = mouseEvent.getX();
            y1 = mouseEvent.getY();
            gc.stroke();
            tag2 = true;
        }
    }

    int result(int[][] pixels) {
        double[] arr;
        arr = Sum(pixels);
        ArrayList<Integer> arr1 = new ArrayList<Integer>();
        arr1.add(0);
        Double maxval = arr[0];
        for (int i = 1; i < 2; i++) {
            if (arr[i] > maxval) {
                arr1.clear();
                arr1.add(i);
                maxval = arr[i];
            }
//                if (arr[i] == maxval) {
//                    arr1.add(i);
//                }
        }
        return arr1.get(0);
    }


    static double[] Sum(int[][] pixels) {
        double[] arr = new double[2];
        //проверить чтобы все элементы инициализировались нулями
        for (int i = 0; i < 32; i++) {
            for (int j = 0; j < 32; j++) {
                if (pixels[i][j] != -1) {
                    for(int ii = 0; ii < 2; ii++){
                        arr[ii] += (double) (W[ii][i][j]);
                    }
                }
            }
        }
        return arr;
    }

    private static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

        final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        final int width = image.getWidth();
        final int height = image.getHeight();
        final boolean hasAlphaChannel = image.getAlphaRaster() != null;

        int[][] result = new int[height][width];
        if (hasAlphaChannel) {
            final int pixelLength = 4;
            for (int pixel = 0, row = 0, col = 0; pixel + 3 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
                argb += ((int) pixels[pixel + 1] & 0xff); // blue
                argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        } else {
            final int pixelLength = 3;
            for (int pixel = 0, row = 0, col = 0; pixel + 2 < pixels.length; pixel += pixelLength) {
                int argb = 0;
                argb += -16777216; // 255 alpha
                argb += ((int) pixels[pixel] & 0xff); // blue
                argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
                argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
                result[row][col] = argb;
                col++;
                if (col == width) {
                    col = 0;
                    row++;
                }
            }
        }

        return result;
    }
}