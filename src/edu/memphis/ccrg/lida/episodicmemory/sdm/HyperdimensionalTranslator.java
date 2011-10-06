/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.episodicmemory.sdm;

import cern.colt.bitvector.BitVector;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import java.util.Collection;

/**
 * This class translates between node structures and bit vectors. The methods
 * used are the ones described by Kanerva in his 2009 hyperdimensional computing
 * paper.
 * @author Rodrigo Silva-Lugo
 */
public class HyperdimensionalTranslator extends BasicTranslator implements Translator {

//    private static final ElementFactory factory = ElementFactory.getInstance();
//    private int size;
//    private PerceptualAssociativeMemory pam;

    /**
     * Constructor of the class.
     * 
     * @param size
     *            the number of positions of the bit vector
     * @param pam
     *            the PAM associated with this translator
     */
    public HyperdimensionalTranslator(int size, PerceptualAssociativeMemory pam) {
        super(size, pam);
    }

    /**
     * 
     * @param data
     * @return 
     */
    @Override
    public NodeStructure translate(BitVector data) {

        // TODO: implement this method.
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * 
     * @param structure
     * @return 
     */
    @Override
    public BitVector translate(NodeStructure structure) {

        // TODO: verify that all nodes in structure are PamNodeImpl?

        // FIXME: add dictionary field, where extra bit vectors are kept.
        Collection<Node> nodes = structure.getNodes();
        int[] sum = new int[this.getSize()];
        BitVector result = new BitVector(this.getSize());
        for (Node n : nodes) {
            PamNode p = n.getGroundingPamNode();
            if (!p.hasSdmId()) {
                p.setSdmId();
            }
            BitVectorUtils.sumVectors(sum, p.getSdmId());
        }
        result = BitVectorUtils.normalizeVector(sum);

        // TODO: Map the sum using multiplication. Initially zero vector?
        return result;
    }
}