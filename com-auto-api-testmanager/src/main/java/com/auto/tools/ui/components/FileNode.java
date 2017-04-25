/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.auto.tools.ui.components;

import java.io.File;

import javax.swing.Icon;

/**
 *
 * @author haojzhou
 */
public class FileNode{
    public FileNode(String name,Icon icon, File file, boolean isDummyRoot){
        this.name=name;this.icon=icon;this.file=file;this.isDummyRoot=isDummyRoot;
    }
    public boolean isInit;
    public boolean isDummyRoot;
    public String name;
    public Icon icon;
    public File file;
    
    public String toString()
    {
    	return this.name;
    }
}
