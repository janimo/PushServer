package org.whispersystems.pushserver.controllers;

import com.codahale.metrics.annotation.Timed;
import org.whispersystems.pushserver.auth.Server;
import org.whispersystems.pushserver.entities.ApnMessage;
import org.whispersystems.pushserver.entities.GcmMessage;
import org.whispersystems.pushserver.entities.UpsMessage;
import org.whispersystems.pushserver.senders.APNSender;
import org.whispersystems.pushserver.senders.GCMSender;
import org.whispersystems.pushserver.senders.UPSSender;
import org.whispersystems.pushserver.senders.TransientPushFailureException;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import io.dropwizard.auth.Auth;

@Path("/api/v1/push")
public class PushController {

  private final APNSender apnSender;
  private final GCMSender gcmSender;
  private final UPSSender upsSender;

  public PushController(APNSender apnSender, GCMSender gcmSender, UPSSender upsSender) {
    this.apnSender = apnSender;
    this.gcmSender = gcmSender;
    this.upsSender = upsSender;
  }

  @Timed
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/gcm")
  public void sendGcmPush(@Auth Server server, @Valid GcmMessage gcmMessage) {
    gcmSender.sendMessage(gcmMessage);
  }

  @Timed
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/apn")
  public void sendApnPush(@Auth Server server, @Valid ApnMessage apnMessage)
      throws TransientPushFailureException
  {
    apnSender.sendMessage(apnMessage);
  }

  @Timed
  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Path("/ups")
  public void sendUpsPush(@Auth Server server, @Valid UpsMessage upsMessage) {
    upsSender.sendMessage(upsMessage);
  }

}
