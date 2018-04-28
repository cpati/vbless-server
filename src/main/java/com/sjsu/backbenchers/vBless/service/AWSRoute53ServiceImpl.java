package com.sjsu.backbenchers.vBless.service;

import java.util.Collection;
import java.util.LinkedList;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.route53.AmazonRoute53Client;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.route53.model.AliasTarget;
import com.amazonaws.services.route53.model.Change;
import com.amazonaws.services.route53.model.ChangeAction;
import com.amazonaws.services.route53.model.ChangeBatch;
import com.amazonaws.services.route53.model.ChangeResourceRecordSetsRequest;
import com.amazonaws.services.route53.model.HostedZone;
import com.amazonaws.services.route53.model.ListHostedZonesRequest;
import com.amazonaws.services.route53.model.ListHostedZonesResult;
import com.amazonaws.services.route53.model.RRType;
import com.amazonaws.services.route53.model.ResourceRecordSet;

@Service
public class AWSRoute53ServiceImpl implements AWSRoute53Service {
	@Value("${aws.access_key_id}")
	private String awsAccessKeyId;

	@Value("${aws.secret_access_key}")
	private String awsSecretKey;
	
	@Value("${s3.region}")
	private String region;
	
	@Value("${aws.hosted_zone_id}")
	private String awsHostedZoneId;
	
	@Value("${aws.hosted_zone_name}")
	private String awsHostedZoneName;
	
	@Value("${aws.alias_hosted_zone_id}")
	private String awsAliasHostedZoneId;
	
	@Value("${aws.alias_hosted_dns_name}")
	private String awsAliasHostedDNSName;
	
	
	public String createTenantDomain(String tenantShortName) {
		AmazonRoute53Client route53Client = new 
    										AmazonRoute53Client(new BasicAWSCredentials(
										    					awsAccessKeyId, 
										    					awsSecretKey));
//    	route53Client.setRegion(Region.getRegion(Regions.fromName(region)));

    	   	
    	ChangeResourceRecordSetsRequest request = new ChangeResourceRecordSetsRequest();
        request.setHostedZoneId(awsHostedZoneId);
        ChangeBatch changeBatch = new ChangeBatch();
        Collection<Change> changes = new LinkedList<Change>();
              
        Change change = new Change();
        change.setAction(ChangeAction.CREATE);
        ResourceRecordSet rrs = new ResourceRecordSet();
        rrs.setType(RRType.A);
        rrs.setName(tenantShortName+"."+awsHostedZoneName);
        AliasTarget at = new AliasTarget();
        at.setHostedZoneId(awsAliasHostedZoneId);
        at.setEvaluateTargetHealth(false);
        at.setDNSName(awsAliasHostedDNSName);
        rrs.setAliasTarget(at);
            
        change.setResourceRecordSet(rrs);
        changes.add(change);
        
        changeBatch.setChanges(changes);
        request.setChangeBatch(changeBatch);
        
        route53Client.changeResourceRecordSets(request);
    	
		return rrs.getName();
	}

}
