package org.recap.util.datadump;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.recap.RecapConstants;
import org.recap.model.csv.DataDumpSuccessReport;
import org.recap.model.jpa.ReportDataEntity;
import org.recap.model.jpa.ReportEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * Created by premkb on 30/9/16.
 */
public class DataDumpSuccessReportGenerator {

    private static final Logger logger = LoggerFactory.getLogger(DataDumpSuccessReportGenerator.class);

    /**
     * Prepare data dump csv success record data dump success report.
     *
     * @param reportEntity the report entity
     * @return the data dump success report
     */
    public DataDumpSuccessReport prepareDataDumpCSVSuccessRecord(ReportEntity reportEntity) {

        List<ReportDataEntity> reportDataEntities = reportEntity.getReportDataEntities();

        DataDumpSuccessReport dataDumpSuccessReport = new DataDumpSuccessReport();

        for (Iterator<ReportDataEntity> iterator = reportDataEntities.iterator(); iterator.hasNext(); ) {
            ReportDataEntity report =  iterator.next();
            String headerName = report.getHeaderName();
            String headerValue = report.getHeaderValue();
            Method setterMethod = getSetterMethod(headerName);
            if(null != setterMethod){
                try {
                    setterMethod.invoke(dataDumpSuccessReport, headerValue);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    logger.error(RecapConstants.ERROR,e);
                }
            }
        }
        return dataDumpSuccessReport;
    }

    /**
     * Gets setter method for the given name.
     *
     * @param propertyName the property name
     * @return the setter method
     */
    public Method getSetterMethod(String propertyName) {
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        try {
            return propertyUtilsBean.getWriteMethod(new PropertyDescriptor(propertyName, DataDumpSuccessReport.class));
        } catch (IntrospectionException e) {
            logger.error(RecapConstants.ERROR,e);
        }
        return null;
    }

    /**
     * Gets getter method for the given name.
     *
     * @param propertyName the property name
     * @return the getter method
     */
    public Method getGetterMethod(String propertyName) {
        PropertyUtilsBean propertyUtilsBean = new PropertyUtilsBean();
        try {
            return propertyUtilsBean.getReadMethod(new PropertyDescriptor(propertyName, DataDumpSuccessReport.class));
        } catch (IntrospectionException e) {
            logger.error(RecapConstants.ERROR,e);
        }
        return null;
    }
}
