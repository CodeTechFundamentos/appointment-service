package com.nutrix.command.api;

import com.nutrix.command.application.services.AppointmentCommandService;
import com.nutrix.command.application.services.DietCommandService;
import com.nutrix.command.domain.Appointment;
import com.nutrix.command.domain.Diet;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/appointment")
@Api(tags = "Appointment", value = "Servicio Web RESTFul de Appointment")
public class AppointmentCommandController {

    @Autowired
    private AppointmentCommandService appointmentService;
    @Autowired
    private DietCommandService dietService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Registro de Appointments", notes = "Método que registra Appointments en BD")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Appointment creado"),
            @ApiResponse(code = 404, message = "Appointment no creado")
    })
    public ResponseEntity<Appointment> insertAppointment(@Valid @RequestBody Appointment appointment) {
        try {
            Diet newDiet = dietService.save(new Diet(appointment.getId(), "", "", appointment.getAppointmentDate()));
            appointment.setDiet(newDiet);
            Appointment appointmentNew = appointmentService.save(appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(appointmentNew);
        } catch (Exception e) {
            return new ResponseEntity<Appointment>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Actualización de datos de Appointment", notes = "Método que actualiza los datos de Appointment")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Datos de Appointment actualizado"),
            @ApiResponse(code = 404, message = "Appointment no encontrado")
    })
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable("id") Integer id, @Valid @RequestBody Appointment appointment) {
        try {
            Optional<Appointment> appointmentUp = appointmentService.getById(id);
            if (!appointmentUp.isPresent())
                return new ResponseEntity<Appointment>(HttpStatus.NOT_FOUND);
            appointment.setId(id);
            appointmentService.save(appointment);
            return new ResponseEntity<Appointment>(appointment, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Appointment>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Eliminación de datos de Appointment", notes = "Método que elimina los datos de Appointment en BD")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Datos de Appointment eliminados"),
            @ApiResponse(code = 404, message = "Appointment no encontrado")
    })
    public ResponseEntity<Appointment> deleteAppointment(@PathVariable("id") Integer id) {
        try {
            Optional<Appointment> appointmentDelete = appointmentService.getById(id);
            if (!appointmentDelete.isPresent())
                return new ResponseEntity<Appointment>(HttpStatus.NOT_FOUND);
            appointmentService.delete(id);
            return new ResponseEntity<Appointment>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Appointment>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/update_appointment_notes/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Actualización de notas de Appointment", notes = "Método que actualiza las notas un Appointment")
    @ApiResponses({
            @ApiResponse(code=201, message = "Datos de Appointment actualizados"),
            @ApiResponse(code=404, message = "Appointment no encontrado")
    })
    public ResponseEntity<Appointment> updateAppointmentNotes(@PathVariable("id") Integer id,
                                                              @Valid @RequestBody Appointment appointment,
                                                              @RequestParam("notes") String notes){
        try{
            Optional<Appointment> appointmentOld = appointmentService.getById(id); //Se encuentra un appointment
            if(!appointmentOld.isPresent()) {
                return new ResponseEntity<Appointment>(HttpStatus.NOT_FOUND);
            }
            else { //Si es así, se actualiza con nuevos datos
                appointment.setId(id);
                appointment.setNutritionistNotes(notes);
                appointmentService.save(appointment);
                return new ResponseEntity<Appointment>(HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<Appointment>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/update_appointment_date/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Actualización de fecha de Appointment", notes = "Método que actualiza la fecha un Appointment")
    @ApiResponses({
            @ApiResponse(code=201, message = "Fecha de Appointment actualizada"),
            @ApiResponse(code=404, message = "Appointment no encontrado")
    })
    public ResponseEntity<Appointment> updateAppointmentDate(@PathVariable("id") Integer id,
                                                             @Valid @RequestBody String date){
        try{
            Optional<Appointment> appointmentOld = appointmentService.getById(id); //Se encuentra un appointment
            if(!appointmentOld.isPresent()) {
                return new ResponseEntity<Appointment>(HttpStatus.NOT_FOUND);
            }
            else { //Si es así, se actualiza con nuevos datos
                Date new_date = ParseDate(date);
                appointmentOld.get().setAppointmentDate(new_date);
                appointmentService.save(appointmentOld.get());
                return new ResponseEntity<Appointment>(HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<Appointment>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/assignDietToAppointment/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Asignación de un Diet a un Appointment", notes = "Método que asigna un Diet a un Appointment")
    @ApiResponses({
            @ApiResponse(code=201, message = "Diet asignado correctamente"),
            @ApiResponse(code=404, message = "Appointment no encontrado")
    })
    public ResponseEntity<Appointment> assignDietToAppointment(@PathVariable("id") Integer id,
                                                               @Valid @RequestBody Diet diet){
        try{
            Optional<Appointment> appointmentOld = appointmentService.getById(id); //Se encuentra un appointment
            if(!appointmentOld.isPresent()) {
                return new ResponseEntity<Appointment>(HttpStatus.NOT_FOUND);
            }
            else { //Si es así, se actualiza con nuevos datos
                appointmentOld.get().setDiet(diet);
                appointmentService.save(appointmentOld.get());
                return new ResponseEntity<Appointment>(HttpStatus.OK);
            }
        }catch (Exception e){
            return new ResponseEntity<Appointment>(HttpStatus.INTERNAL_SERVER_ERROR);
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

}
