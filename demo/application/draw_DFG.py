import os
from pm4py.algo.discovery.dfg import factory as dfg_factory
from pm4py.visualization.dfg import factory as dfg_vis_factory
from pm4py.objects.log.importer.xes import factory as xes_importer

#
#
#
# log = xes_importer.import_log(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI_2013.xes")
# dfg = dfg_factory.apply(log)
# gviz = dfg_vis_factory.apply(dfg, log=log, variant="performance", parameters=parameters)



def draw_DFG(dfg):
    parameters = {"format": "svg"}
    gviz = dfg_vis_factory.apply(dfg,  variant="performance", parameters=parameters)
    log = xes_importer.import_log(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI_2013.xes")
    dfg = dfg_factory.apply(log)
    gviz = dfg_vis_factory.apply(dfg, log=log, variant="performance", parameters=parameters)
    dfg_vis_factory.save(gviz, "dfg.svg")