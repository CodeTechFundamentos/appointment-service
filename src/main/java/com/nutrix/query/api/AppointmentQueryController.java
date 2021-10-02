package com.nutrix.query.api;

import com.nutrix.command.domain.Appointment;
import com.nutrix.query.application.services.AppointmentQueryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointment")
@Api(tags = "Appointment", value = "Servicio Web RESTFul de Appointment")
public class AppointmentQueryController {

    @Autowired
    private AppointmentQueryService appointmentService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Listar Appointments", notes = "Método para listar todos los Appointments")
    @ApiResponses({
            @ApiResponse(code=201, message = "Appointments encontrados"),
            @ApiResponse(code=404, message = "Appointments no encontrados")
    })
    public ResponseEntity<List<Appointment>> findAll(){
        try{
            List<Appointment> appointments = appointmentService.getAll();
            if(appointments.size()>0)
                return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
            else
                return new ResponseEntity<List<Appointment>>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<List<Appointment>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Buscar Appointment por Id", notes = "Método para listar un Appointment por Id")
    @ApiResponses({
            @ApiResponse(code=201, message = "Appointment encontrado"),
            @ApiResponse(code=404, message = "Appointment no encontrado")
    })
    public ResponseEntity<Appointment> findAppointmentById(@PathVariable("id") Integer id){
        try{
            Optional<Appointment> appointment = appointmentService.getById(id);
            if(!appointment.isPresent())
                return new ResponseEntity<Appointment>(HttpStatus.NOT_FOUND);
            else
                return new ResponseEntity<Appointment>(appointment.get(), HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<Appointment>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/searchBetweenDates", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Buscar Appointments entre fechas", notes = "Método para listar Appointments entre fechas")
    @ApiResponses({
            @ApiResponse(code=201, message = "Appointments encontrados"),
            @ApiResponse(code=404, message = "Appointments no encontrados")
    }) //Al requestparam le puedes decir que sea opcional y no necesita estar en el URL
    public ResponseEntity<List<Appointment>> findAppointmentByAppointmentDateBetweenDates(@RequestParam("date1") String date1_string,
                                                                                          @RequestParam("date2") String date2_string){
        try{
            Date checking_date = ParseDate(date1_string);
            Date checkout_date = ParseDate(date2_string);
            List<Appointment> appointments = appointmentService.findBetweenDates(checking_date, checkout_date);
            if(appointments!=null && appointments.size()>0)
                return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
            else
                return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<List<Appointment>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public static Date ParseDate(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date result = null;
        try{
            result = format.parse(date);
        }catch (Exception e){
        }
        return result;
    }

    @GetMapping(value = "/searchAppointmentByNutritionistId/{nutritionist_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Buscar Appointments por nutritionist id", notes = "Método para encontrar Appointments por nutritionist id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Appointments encontrados"),
            @ApiResponse(code = 404, message = "Appointments no ubicados")
    })
    public ResponseEntity<List<Appointment>> findByNutritionist(@PathVariable("nutritionist_id") Integer nutritionist_id)
    {
        try{
            List<Appointment> appointments = appointmentService.findByNutritionist(nutritionist_id);
            if(appointments.size()>0)
                return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
            return new ResponseEntity<List<Appointment>>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<List<Appointment>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/searchAppointmentByPatientId/{patient_id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Buscar Appointments por patient id", notes = "Método para encontrar Appointments por patient id")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Appointments encontrados"),
            @ApiResponse(code = 404, message = "Appointments no ubicados")
    })
    public ResponseEntity<List<Appointment>> findByPatient(@PathVariable("patient_id") Integer patient_id)
    {
        try{
            List<Appointment> appointments = appointmentService.findByPatient(patient_id);
            if(appointments.size()>0)
                return new ResponseEntity<List<Appointment>>(appointments, HttpStatus.OK);
            return new ResponseEntity<List<Appointment>>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<List<Appointment>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
