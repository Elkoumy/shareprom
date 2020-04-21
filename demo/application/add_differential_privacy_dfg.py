import diffprivlib.mechanisms as privacyMechanisms
import sys

def add_noise_to_dfg(DFG_matrix,epsilon):
    laplace_mechanism = privacyMechanisms.LaplaceBoundedDomain()
    laplace_mechanism.set_sensitivity(1).set_bounds(0,sys.maxsize).set_epsilon(epsilon)
    return [int(round(laplace_mechanism.randomise(x))) for x in DFG_matrix]
