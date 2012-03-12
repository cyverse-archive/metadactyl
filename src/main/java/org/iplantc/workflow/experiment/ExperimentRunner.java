package org.iplantc.workflow.experiment;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import net.sf.json.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.iplantc.files.service.FileInfoService;
import org.iplantc.hibernate.util.HibernateAccessor;
import org.iplantc.workflow.client.OsmClient;
import org.iplantc.workflow.dao.DaoFactory;
import org.iplantc.workflow.dao.hibernate.HibernateDaoFactory;
import org.iplantc.workflow.experiment.util.JobConfigUtils;
import org.iplantc.workflow.service.UserService;
import org.iplantc.workflow.user.UserDetails;

/**
 * This is a stub class for executing experiments
 * 
 * @author Juan Antonio Raygoza Garay
 */
public class ExperimentRunner extends HibernateAccessor {
    public static final String CONDOR_TYPE = "condor";
    private static final Logger LOG = Logger.getLogger(ExperimentRunner.class);
    private static final Logger JsonLogger = Logger.getLogger("JsonLogger");

    private FileInfoService fileInfo;

    private UserService userService;

    private String executionUrl;
    private UrlAssembler urlAssembler;

    private OsmClient jobRequestOsmClient;

    public ExperimentRunner() {

    }

    public void runExperiment(JSONObject experiment) throws Exception {
        Session session = getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();
            runExperiment(experiment, session);
            tx.commit();
        }
        catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
        finally {
            if (session.isOpen()) {
                session.close();
            }
        }

    }

    private void runExperiment(JSONObject experiment, Session session) throws Exception {
        LOG.debug("Running experiment: " + experiment);
        JsonLogger.info("runExperiment received the following input: " + experiment.toString(2));

        try {
            long workspaceId = Long.parseLong(experiment.getString("workspace_id"));
            UserDetails userDetails = userService.getCurrentUserDetails();

            JSONObject job = formatJobRequest(experiment, session, userDetails);
            storeJobSubmission(experiment, job.getString("uuid"));
            submitJob(job);
        }
        catch (Exception ex) {
            JsonLogger.error("Caught exception when processing");
            throw new Exception("ExperimentRunner ", ex);
        }
    }

    private void storeJobSubmission(JSONObject experiment, String jobUuid) {
        JSONObject state = new JSONObject();
        state.put("jobUuid", jobUuid);
        state.put("experiment", JobConfigUtils.escapeJobConfig(experiment));
        String uuid = jobRequestOsmClient.save(state);
        if (LOG.isDebugEnabled()) {
            LOG.debug("job request stored for job " + jobUuid + " with object persistence uuid " + uuid);
        }
    }

	protected JSONObject formatJobRequest(JSONObject experiment, Session session, UserDetails userDetails) {
        DaoFactory daoFactory = new HibernateDaoFactory(session);
        JobNameUniquenessEnsurer jobNameUniquenessEnsurer = new TimestampJobNameUniquenessEnsurer();
        JobRequestFormatterFactory factory = new JobRequestFormatterFactory(daoFactory, fileInfo, urlAssembler,
            userDetails, jobNameUniquenessEnsurer);
        return factory.getFormatter(experiment).formatJobRequest();
    }

    protected void submitJob(JSONObject job) throws UnsupportedEncodingException, IOException, ClientProtocolException {
        /** send message **/
        HttpClient client = new DefaultHttpClient();
        client.getParams().setIntParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        LOG.debug("Execution url: " + executionUrl);
        HttpPost post = new HttpPost(executionUrl);
        LOG.debug("Job: " + job);

        JsonLogger.info("Returning from runExperiment with the following result: "
                + job.toString(2));
        post.setEntity(new StringEntity(job.toString(), "application/json", "UTF-8"));

        HttpResponse response = client.execute(post);
        LOG.debug("Response from HttpClient post: " + response.getStatusLine().getStatusCode());
    }

    public FileInfoService getFileInfo() {
        return fileInfo;
    }

    public void setFileInfo(FileInfoService fileInfo) {
        this.fileInfo = fileInfo;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getExecutionUrl() {
        return executionUrl;
    }

    public void setExecutionUrl(String executionUrl) {
        this.executionUrl = executionUrl;
    }

    public void setUrlAssembler(UrlAssembler urlAssembler) {
        this.urlAssembler = urlAssembler;
    }

    public UrlAssembler getUrlAssembler() {
        return urlAssembler;
    }

    public void setJobRequestOsmClient(OsmClient jobRequestOsmClient) {
        this.jobRequestOsmClient = jobRequestOsmClient;
    }

    public OsmClient getJobRequestOsmClient() {
        return jobRequestOsmClient;
    }
}
