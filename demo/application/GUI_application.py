import tkinter as tk
from tkinter import filedialog, Text
import os
from svglib.svglib import svg2rlg
from reportlab.graphics import renderPDF, renderPM
from PIL import Image, ImageTk
from preprocessing_old import preprocessing_partyA
from preprocessing import read_xes, endcoding_events, padding_log, building_sharemind_model, preprocessing
from upload_to_sharemind import upload
from submit_job_to_sharemind import submit
from parse_results import parse_results
from convert_DFG import convert_DFG_to_matrix,convert_DFG_to_counter
from draw_DFG import draw_DFG


root = tk.Tk( ) # the object that contains everything
# we add a canvas to put components on it and control the size
# canvas = tk.Canvas(root, height= 700, width= 700, bg="#263D42")
root.config( bg="white")

file=[]
dataset_name= "demo"
input_dir=r"data/"
output_dir=r"data/"
log_dir= r"/DFG_log/DFG.out"
no_of_chunks = 1
event_a = 9
event_b = 3
partyB_event_names={'"order intermediate B"': '001000000000', '"produce intermediate B"': '010000000000', '"Transport intermediate B"': '100000000000'}
event_names={}

def add_xes():
    #opening a browse file dialog
    filename= filedialog.askopenfilename(initialdir=r"DFG_log", title="Select File",
                                         filetypes=(("XES","*.xes"), ("all files", "*.*")))
    file.append(filename)

    if list_box.get(0)=="No Files Selected. Please add a file to be processed!":
        list_box.delete(0)
    list_box.insert(len(file), filename)


    total_activities = event_a + event_b

    data, activities_count, event_per_case = read_xes(filename)
    # event_per_case is going to be used when uploading the file to sharemind

    # encoding start =0 for party A
    event_names = preprocessing(data, total_activities, 0, dataset_name, "party_A", output_dir)

    upload(output_dir, dataset_name, "party_A")


def run_dfg():
    #call the functions here

    log_dir = r"DFG_log/DFG.out"
    submit(no_of_chunks, dataset_name, event_a, event_b, log_dir)

    parse_results(log_dir)

    #
    out_dir = r"DFG_log/DFG.log"
    freq, time = convert_DFG_to_matrix(out_dir)
    for key in partyB_event_names.keys():
        event_names[key] = partyB_event_names[key]
    dfg = convert_DFG_to_counter(time, event_names)
    draw_DFG(dfg)

    """ view the dfg diagram on the canvas"""
    drawing = svg2rlg("dfg.svg")
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