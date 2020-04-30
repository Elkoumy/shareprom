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
from convert_DFG import convert_DFG_to_matrix,convert_DFG_to_counter,convert_DFG_to_dataframe
from draw_DFG import draw_DFG
from add_differential_privacy_dfg import  add_noise_to_dfg
from pm4py.objects.log.importer.xes import factory as xes_import_factory
from pm4py.visualization.dfg import factory as dfg_vis_factory
from pm4py.algo.discovery.dfg import factory as dfg_factory

root = tk.Tk( ) # the object that contains everything
# we add a canvas to put components on it and control the size
# canvas = tk.Canvas(root, height= 700, width= 700, bg="#263D42")
root.config( bg="white")
root.title("Shareprom")
root.wm_iconbitmap(r"GUI_images/phoenix_icon.ico")

file=[]


dataset_name= "demo"
input_dir=r"data/"
output_dir=r"data/"
log_dir= r"/DFG_log/DFG.out"
no_of_chunks = 1
event_a = 9
event_b = 3
partyB_event_names={'"order intermediate B"': '001000000000', '"produce intermediate B"': '010000000000', '"Transport intermediate B"': '100000000000'}
event_names={'"order"': '000000000001', '"process order"': '000000000010', '"calculate demand intermediate B"': '000000000100', '"arrival intermediate B"': '000000001000', '"quick test intermediate B"': '000000010000', '"Production"': '000000100000', '"final test"': '000001000000', '"prepare delivery"': '000010000000', '"delivery of product"': '000100000000'}

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
    event_names_temp = preprocessing(data, total_activities, 0, dataset_name, "party_A", output_dir)

    upload(output_dir, dataset_name, "party_A")


def run_dfg():
    #call the functions here

    partyB_event_names = {'"order intermediate B"': '001000000000', '"produce intermediate B"': '010000000000',
                          '"Transport intermediate B"': '100000000000'}
    event_names = {'"order"': '000000000001', '"process order"': '000000000010',
                   '"calculate demand intermediate B"': '000000000100', '"arrival intermediate B"': '000000001000',
                   '"quick test intermediate B"': '000000010000', '"Production"': '000000100000',
                   '"final test"': '000001000000', '"prepare delivery"': '000010000000',
                   '"delivery of product"': '000100000000'}
    log_dir = r"DFG_log/DFG.out"
    submit(no_of_chunks, dataset_name, event_a, event_b, log_dir)

    parse_results(log_dir)

    #
    out_dir = r"DFG_log/DFG.log"
    freq, time = convert_DFG_to_matrix(out_dir)

    ''' apply differential privacy here  '''
    freq, time = add_noise_to_dfg(freq, time, 10)
    freq, time = convert_DFG_to_dataframe(out_dir, freq, time)
    for key in partyB_event_names.keys():
        event_names[key] = partyB_event_names[key]
    dfg = convert_DFG_to_counter(time, event_names)
    draw_DFG(dfg)

    """ view the dfg diagram on the canvas"""
    # drawing = svg2rlg("dfg.svg")
    drawing = svg2rlg("dfg_out.svg")
    renderPM.drawToFile(drawing, "dfg.png", fmt="PNG")
    img = Image.open('dfg.png')

    win = tk.Toplevel()
    win.wm_title("Window")
    win.wm_iconbitmap(r"GUI_images/phoenix_icon.ico")
    l = tk.Label(win, text="")
    img = tk.PhotoImage(file="dfg.png")
    l.config(image=img)
    l.image = img
    l.grid(row=0, column=0)

    return

def view_model():
    file=list_box.get(list_box.curselection())

    log = xes_import_factory.apply(file)
    parameters = {"format": "svg"}
    dfg = dfg_factory.apply(log)
    gviz = dfg_vis_factory.apply(dfg,log,  variant="performance", parameters=parameters)
    dfg_vis_factory.save(gviz, "party_model.svg")

    drawing = svg2rlg("party_model.svg")
    renderPM.drawToFile(drawing, "party_model.png", fmt="PNG")

    win = tk.Toplevel()
    win.wm_title("Viewing Model of this Party Only")
    win.wm_iconbitmap(r"GUI_images/phoenix_icon.ico")
    l = tk.Label(win, text="")
    img = tk.PhotoImage(file="party_model.png")
    l.config(image=img)
    l.image = img
    l.grid(row=0, column=0)
    return



#to add a frame inside the canvas
#frame= tk.Frame(root, bg="white")
#frame.place(relwidth=0.8, relheight=0.8, relx=0.1, rely=0.1)


#the shareprom title
title= tk.Label(root, text="Shareprom-  a Secure Multi-party computation system\n for Inter-organizational Process Mining", font='Helvetica 14 bold', bg="white")
title.pack()

'''open file Button'''
openFile = tk.Button(root, text = 'Click Me !',    command=add_xes,  bg="white")
img = tk.PhotoImage(file=r"GUI_images/add_file.png",height = 40, width = 150)
openFile.config(image=img)
openFile.pack(padx=5, pady=10, side=tk.TOP)
openFile.image=img

'''View Model Button'''
viewModel = tk.Button(root, text = 'Click Me !',    command=view_model,  bg="white",height = 40, width = 150)
img = tk.PhotoImage(file=r"GUI_images/view_model.png")
viewModel.config(image=img)
viewModel.pack()
viewModel.image=img



### list view for listing the files
list_label = tk.Label(root, text="Uploaded File", font='Helvetica 10 bold', bg="white")
list_box = tk.Listbox(root,width=40, height=10 )
list_box.insert(1,"No Files Selected. Please add a file to be processed!")
list_label.pack()
list_box.pack()




#### build dfg button
build_dfg = tk.Button(root, text="Build DFG", command = run_dfg,  bg="white",height = 40, width = 170)
img = tk.PhotoImage(file=r"GUI_images/calculate_dfg.png")
# build_dfg.grid(row=0)
build_dfg.config(image=img)
build_dfg.pack()
build_dfg.image=img



### University of Tartu Logo
tartu = tk.Label(root, text="", bg="white")
img = tk.PhotoImage(file=r"GUI_images/tartu.png")
img = img.subsample(2)
tartu.config(image=img)
tartu.image = img
tartu.pack(padx=5, pady=10, side=tk.LEFT)

### Huberlin Logo
huberlin = tk.Label(root, text="", bg="white")
img = tk.PhotoImage(file=r"GUI_images/Huberlin-logo.png")
img = img.subsample(6)
huberlin.config(image=img)
huberlin.image = img
huberlin.pack(padx=5, pady=20, side=tk.LEFT)

### Cyber Logo
cyber = tk.Label(root, text="", bg="white")
img = tk.PhotoImage(file=r"GUI_images/cybernetica.png")
img = img.subsample(2)
cyber.config(image=img)
cyber.image = img
cyber.pack(padx=5, pady=30, side=tk.LEFT)
#start the application GUI
root.mainloop()