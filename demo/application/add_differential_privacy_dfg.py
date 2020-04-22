import diffprivlib.mechanisms as privacyMechanisms
import sys

def add_noise_to_dfg(DFG_matrix,time_matrix,epsilon):
    laplace_mechanism = privacyMechanisms.LaplaceBoundedDomain()
    laplace_mechanism.set_sensitivity(1).set_bounds(0,sys.maxsize).set_epsilon(epsilon)
    anonymizied_DFG_matrix = list()
    anonymizied_time_matrix = list()
    for i in range(0,len(DFG_matrix)):
        anonymizied_DFG_matrix.append(int(round(laplace_mechanism.randomise(DFG_matrix[i]))))
        if anonymizied_DFG_matrix[i] == 0:
            anonymizied_time_matrix.append(0)
        else:
            anonymizied_time_matrix.append(time_matrix[i])
    return anonymizied_DFG_matrix, anonymizied_time_matrix