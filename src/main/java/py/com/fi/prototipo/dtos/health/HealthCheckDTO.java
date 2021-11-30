package py.com.fi.prototipo.dtos.health;

import java.util.Date;

/**
 * @author wtorales
 * @fecha 30/11/2021
 */
public class HealthCheckDTO {
    public enum HEALTH_STATUS {UP//está funcionando correctamente
        , DOWN,//no está funcionando, pero puede recuperarse
        FATAL//no está funcionando y ya no se recuperará
    };

    /**
     * El nombre del servicio que se comprobo
     */
    private String componentName;
    /**
     * La fecha de comprobación
     */
    private Date testDate;

    private HEALTH_STATUS status;
    private long elapsedTime;

    private String lastErrorMessage;
    private String lastException;

    public HealthCheckDTO(String pComponentName) {
        componentName = pComponentName;
        this.testDate=new Date();
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String pComponentName) {
        componentName = pComponentName;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date pTestDate) {
        testDate = pTestDate;
    }

    public HEALTH_STATUS getStatus() {
        return status;
    }

    public void setStatus(HEALTH_STATUS pStatus) {
        status = pStatus;
    }

    public long getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(long pElapsedTime) {
        elapsedTime = pElapsedTime;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void setLastErrorMessage(String pLastErrorMessage) {
        lastErrorMessage = pLastErrorMessage;
    }

    public String getLastException() {
        return lastException;
    }

    public void setLastException(String pLastException) {
        lastException = pLastException;
    }
}
