package com.fuckingcheese;

import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;



public class IvanProvider {
    private XSSFWorkbook wb = null;
    private ArrayList<Double> XData = new ArrayList<>();
    private ArrayList<Double> YData = new ArrayList<>();
    private ArrayList<Double> ZData = new ArrayList<>();
    private ArrayList<Double> XResult = new ArrayList<>();
    private ArrayList<Double> YResult = new ArrayList<>();
    private ArrayList<Double> ZResult = new ArrayList<>();
    private double CXY;
    private double CYZ;
    private double CXZ;
    private double SMY;
    private double GMZ;
    private double SMZ;
    FlatQuestion fq = new FlatQuestion();
    public Boolean loadFile(File file) // получение рабочей книги из файла
    {
        try {
            wb = new XSSFWorkbook(file);
            if(processFile())
                return true;
            else
                return false;
        } catch (InvalidFormatException ex) {
            System.out.println("You");
            Logger.getLogger(IvanProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println("Fuck");
            Logger.getLogger(IvanProvider.class.getName()).log(Level.SEVERE, null, ex);
        } catch (org.apache.poi.openxml4j.exceptions.InvalidFormatException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e)
        {
            System.out.println("Fuck You");
            MATSTAR frame = new MATSTAR();
            JOptionPane.showMessageDialog(frame,"где файл долбаеб");
        }
        return false;
    }

    public Boolean processFile()
    {
        XSSFSheet curShit = wb.getSheet("Вариант 2");
        if (curShit == null)
        {
            MATSTAR frame = new MATSTAR();
            JOptionPane.showMessageDialog(frame,"где мой вариант долбаеб");
            return false;
        }
        else {
            XData.addAll(getMATSTAT(curShit,0));
            YData.addAll(getMATSTAT(curShit,1));
            ZData.addAll(getMATSTAT(curShit,2));
            return true;
//            System.out.println(fq.geometricMean(XData));
//            System.out.println(fq.simpleMean(XData));
//            writeResult();
        }
    }
    public void calculateValues()
    {
        if(XData == null)
        {
            MATSTAR frame = new MATSTAR();
            JOptionPane.showMessageDialog(frame,"где эксель???");
        }
        else {
            //для X
            XResult = fq.calculate(XData);
            //Y
            YResult = fq.calculate(YData);
            //Z
            ZResult = fq.calculate(ZData);

            System.out.println(XResult);
            double[] dr = getArray(XData);
            double[] yr = getArray(YData);
            double[] zr = getArray(ZData);
            CXY = new Covariance().covariance(dr,yr);
            CXZ = new Covariance().covariance(yr,zr);
            CYZ = new Covariance().covariance(dr,zr);
            XResult.add(CXY);
            YResult.add(CYZ);
            ZResult.add(CXZ);


            SMY = 0.0;
            GMZ = 0.0;
            SMZ = 0.0;
        }
    }
    public double[] getArray(ArrayList<Double> convarr)
    {
        double[] rr = new double[convarr.size()];
        int i=0;
        for (Double d:convarr) {
            rr[i] = d;
            i++;
        }
        //System.out.println(rr[50]);
        return rr;
    }

    public void writeResult() {
        if(CXY == 0.0)
        {
            MATSTAR frame = new MATSTAR();
            JOptionPane.showMessageDialog(frame,"че писать???");
        }
        else {
            Workbook exl = new XSSFWorkbook();
            Sheet shit0 = exl.createSheet("Shit");
            Row rowx = shit0.createRow(0); // создали не посредственно строку в параметр указываем номер строки
            Row rowy = shit0.createRow(1);
            Row rowz = shit0.createRow(2);

            ArrayList<Cell> cell0 = new ArrayList<>();
            ArrayList<Cell> cell1 = new ArrayList<>();
            ArrayList<Cell> cell2 = new ArrayList<>();

            for (int i = 0;i<XResult.size();i++) {
                cell0.add(rowx.createCell(i));
                cell0.get(i).setCellValue(XResult.get(i));
            }

            for (int i = 0;i<YResult.size();i++) {
                cell1.add(rowx.createCell(i));
                cell1.get(i).setCellValue(YResult.get(i));
            }

            for (int i = 0;i<YResult.size();i++) {
                cell2.add(rowx.createCell(i));
                cell2.get(i).setCellValue(YResult.get(i));
            }

//            Cell cellx0 = rowx.createCell(0);
//            Cell cellx1 = rowx.createCell(1);
//            cellx0.setCellValue(CXY);
//            cellx1.setCellValue(CXY);
//
//            Cell celly0 = rowy.createCell(0);
//            Cell celly1 = rowy.createCell(1);
//            celly0.setCellValue(CYZ);
//            celly1.setCellValue(CYZ);
//
//            Cell cellz0 = rowz.createCell(0);
//            Cell cellz1 = rowz.createCell(1);
//            cellz0.setCellValue(CXZ);
//            cellz1.setCellValue(CXZ);
            //запись
            try {
                FileOutputStream fil = new FileOutputStream("/Users/kubasya/Documents/MyFolder/ProgramingsStuff/MyResultTable.xls");
                exl.write(fil);
                System.out.println("суцесс");
                fil.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public ArrayList<Double> getMATSTAT(XSSFSheet shit, int B)
    {
        XSSFRow row = shit.getRow(0);
        XSSFCell cell;
        int A=0;   // наибольшее количество строк в колонке на листе, при считывании колонок с таким количеством строк цикл не прооставляет А
        ArrayList<Double> data = new ArrayList();
        for(int i = 0; i < 999; i++)     // получение максимального количества строк в колонке
        {
            row = shit.getRow(i);
            if(shit.getRow(i+1) != null && row.getCell(B) == null){
                //cell = row.getCell(B);
                A=i;
                break;
            }
            if(shit.getRow(i+1) != null && shit.getRow(i+1).getCell(B) == null)
            {
                A=i+1;
                break;
            }
            if(shit.getRow(i+1) == null && row.getCell(B) != null)
            {
                A=i+1;
                break;
            }
        }
        //System.out.println(A);
        for(int i = 1; i < A; i++)
        {
            row = shit.getRow(i);
            data.add(row.getCell(B).getNumericCellValue());
        }
       // System.out.println(data);
        return data;
    }
}
