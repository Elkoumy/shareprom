import tkinter as tk
from tkinter import filedialog, Text
import os
from svglib.svglib import svg2rlg
from reportlab.graphics import renderPDF, renderPM
from PIL import Image, ImageTk


root = tk.Tk() # the object that contains everything

file=[]
def add_xes():
    #opening a browse file dialog
    filename= filedialog.askopenfilename(initialdir=r"DFG_log", title="Select File",
                                         filetypes=(("XES","*.xes"), ("all files", "*.*")))
    file.append(filename)
    print(filename)

def run_dfg():
    #call the functions here
    return

# we add a canvas to put components on it and control the size
canvas = tk.Canvas(root, height= 700, width= 700, bg="#263D42")

#to add a frame inside the canvas
#frame= tk.Frame(root, bg="white")
#frame.place(relwidth=0.8, relheight=0.8, relx=0.1, rely=0.1)

#labels
# label= tk.Label(frame, text="asdfadf")
# label.pack()

#making buttons (docked to root) the function on click is add_app
openFile = tk.Button(root, text="Add XES file", padx=10, pady=5, fg="white", bg="#263D42", command=add_xes)
openFile.pack()


build_dfg = tk.Button(root, text="Build DFG", padx=10, pady=5, fg="white", bg="#263D42" , command = run_dfg)
build_dfg.pack()


""" view the dfg diagram on the canvas"""

drawing = svg2rlg("dfg.svg")
renderPM.drawToFile(drawing, "dfg.png", fmt="PNG")
canvas.pack()
img = Image.open('dfg.png')
pimg = ImageTk.PhotoImage(img)
size = img.size

canvas.create_image(0,0,anchor='nw',image=pimg)



#start the application GUI
root.mainloop()