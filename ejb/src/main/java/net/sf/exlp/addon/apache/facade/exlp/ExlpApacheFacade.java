package net.sf.exlp.addon.apache.facade.exlp;

import net.sf.exlp.addon.apache.ejb.ExlpApache;
import net.sf.exlp.addon.common.data.facade.exlp.ExlpFacade;

public interface ExlpApacheFacade extends ExlpFacade
{
	ExlpApache nExlpApache(ExlpApache apache);
	
	// Matlab
	double[][] dHitsPerMonth(Double minYear);
	double[][] dHitsPerDay(Double minYear);
}
