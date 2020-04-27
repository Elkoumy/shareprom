import tkinter as tk
from tkinter import filedialog, Text
import os
from svglib.svglib import svg2rlg
from reportlab.graphics import renderPDF, renderPM
from PIL import Image, ImageTk



root = tk.Tk( ) # the object that contains everything
# we add a canvas to put components on it and control the size
# canvas = tk.Canvas(root, height= 700, width= 700, bg="#263D42")
root.config( bg="white")

file=[]
def add_xes():
    #opening a browse file dialog
    filename= filedialog.askopenfilename(initialdir=r"DFG_log", title="Select File",
                                         filetypes=(("XES","*.xes"), ("all files", "*.*")))
    file.append(filename)

    if list_box.get(0)=="No Files Selected. Please add a file to be processed!":
        list_box.delete(0)
    list_box.insert(len(file), filename)


def run_dfg():
    #call the functions here
    """ view the dfg diagram on the canvas"""
    drawing = svg2rlg("manufacurer.svg")
    renderPM.drawToFile(drawing, "dfg.png", fmt="PNG")
    img = Image.open('dfg.png')

    win = tk.Toplevel()
    win.wm_title("Window")

    l = tk.Label(win, text="")
    img = tk.PhotoImage(file="dfg.png")
    l.config(image=img)
    l.image = img
    l.grid(row=0, column=0)

    return




#to add a frame inside the canvas
#frame= tk.Frame(root, bg="white")
#frame.place(relwidth=0.8, relheight=0.8, relx=0.1, rely=0.1)


#the shareprom title
title= tk.Label(root, text="Shareprom-  a Secure Multi-party computation system for Inter-organizational Process Mining", font='Helvetica 14 bold', bg="white")
title.pack()


#making buttons (docked to root) the function on click is add_app
openFile = tk.Button(root, text = 'Click Me !',    command=add_xes,  bg="white")
img = tk.PhotoImage(file=r"GUI_images/add_file.png")
openFile.config(image=img)
openFile.pack()
openFile.image=img


### list view for listing the files
list_label = tk.Label(root, text="Uploaded File", font='Helvetica 10 bold', bg="white")
list_box = tk.Listbox(root,width=50, height=20)
list_box.insert(1,"No Files Selected. Please add a file to be processed!")
list_label.pack()
list_box.pack()


#### build dfg button
build_dfg = tk.Button(root, text="Build DFG", command = run_dfg,  bg="white")
img = tk.PhotoImage(file=r"GUI_images/calculate_dfg.png")
# build_dfg.grid(row=0)
build_dfg.config(image=img)
build_dfg.pack()
build_dfg.image=img


### University of Tartu Logo
tartu = tk.Label(root, text="", bg="white")
img = tk.PhotoImage(file=r"GUI_images/tartu.png")
tartu.config(image=img)
tartu.image = img
tartu.pack()
#start the application GUI
root.mainloop()