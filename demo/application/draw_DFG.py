import os
from pm4py.objects.log.importer.xes import factory as xes_importer

log = xes_importer.import_log(r"C:\Gamal Elkoumy\PhD\OneDrive - Tartu Ülikool\Secure MPC\Business Process Mining SourceCode\Datasets\BPI_2013.xes")


from pm4py.algo.discovery.dfg import factory as dfg_factory

dfg = dfg_factory.apply(log)

from pm4py.visualization.dfg import factory as dfg_vis_factory

# gviz = dfg_vis_factory.apply(dfg, log=log, variant="frequency")

dfg = dfg_factory.apply(log, variant="performance")

print(dfg)
parameters = {"format":"svg"}
gviz = dfg_vis_factory.apply(dfg, log=log, variant="performance", parameters=parameters)
dfg_vis_factory.save(gviz, "dfg.svg")