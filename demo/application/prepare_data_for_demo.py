
from pm4py.objects.log.importer.xes import factory as xes_import_factory
from pm4py.objects.conversion.log.versions.to_dataframe import get_dataframe_from_event_stream
from pm4py.objects.log.importer.csv import factory as csv_importer
from pm4py.objects.log.exporter.xes import factory as xes_exporter
from pm4py.algo.discovery.dfg import factory as dfg_factory
from pm4py.visualization.dfg import factory as dfg_vis_factory

Manufacturer=['"order"','"delivery of product"', '"process order"', '"calculate demand intermediate B"','"arrival intermediate B"','"quick test intermediate B"','"Production"','"final test"','"prepare delivery"' ]

supplier_B=[ '"order intermediate B"','"produce intermediate B"','"Transport intermediate B"']

xes_file="super.xes"
log = xes_import_factory.apply(xes_file)
data =get_dataframe_from_event_stream(log)

data=data[data["lifecycle:transition"].isin(['complete'])]

manufacurer_df = data[data["concept:name"].isin( Manufacturer+supplier_B)]

supplier_B_df= data[data["concept:name"].isin( supplier_B)]


# event_stream = csv_importer.import_event_stream('manufacurer.csv')

xes_exporter.export_log(manufacurer_df, "manufacurer.xes")
xes_exporter.export_log(supplier_B_df, "supplier_B.xes")

log = xes_import_factory.apply("manufacurer.xes")


dfg = dfg_factory.apply(log)
parameters = {"format": "svg"}
gviz = dfg_vis_factory.apply(dfg, log=log, variant="performance", parameters=parameters)
dfg_vis_factory.save(gviz, "manufacurer.svg")


log = xes_import_factory.apply("supplier_B.xes")

dfg = dfg_factory.apply(log)
parameters = {"format": "svg"}
gviz = dfg_vis_factory.apply(dfg, log=log, variant="performance", parameters=parameters)
dfg_vis_factory.save(gviz, "supplier_B.svg")