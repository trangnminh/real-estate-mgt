package eeet2582.realestatemgt.controller;

import eeet2582.realestatemgt.model.House;
import eeet2582.realestatemgt.model.form.HouseForm;
import eeet2582.realestatemgt.model.helper.HouseSearchForm;
import eeet2582.realestatemgt.service.HouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static eeet2582.realestatemgt.service.HouseService.HOUSE_BATCH_SIZE;

/*
NON-AUTHORIZED AND AUTHORIZED USER CAN:
- getHousesBySearchForm : get houses filtered by a comprehensive search form
- getHouseById : see each house in details
*/

/*
ADMIN CAN:
- addNewHouse : add new house into database
- updateHouseById : update an existing house by id
- addHouseImage : add more images into an existing house
- deleteHouseById : delete house by id
 */

@RestController
@RequestMapping("api/v1/houses")
@CrossOrigin(origins = "*")
public class HouseController {

    private final HouseService houseService;

    @Autowired
    public HouseController(HouseService houseService) {
        this.houseService = houseService;
    }

    @GetMapping("random/{num}")
    public List<House> getRandomHouses(@PathVariable int num) {
        return houseService.getRandomHouses(num);
    }

    @PostMapping("/search/form")
    public Page<House> getHousesBySearchForm(@RequestBody HouseSearchForm form,
                                             @RequestParam(value = "pageNo", defaultValue = "1") int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                             @RequestParam(value = "orderBy", defaultValue = "asc") String orderBy) {
        System.out.println(form);

        int visitedEntries = Math.max(0, (pageNo - 1) * pageSize);
        int batchNo = visitedEntries / HOUSE_BATCH_SIZE;
        List<House> batch = houseService.getHousesBySearchForm(form);

        if (orderBy.equals("asc"))
            batch.sort(Comparator.comparing(House::getPrice));
        else
            batch.sort(Comparator.comparing(House::getPrice).reversed());

        return getPageFromBatch(pageNo, pageSize, visitedEntries, batchNo, HOUSE_BATCH_SIZE, batch);
    }

    public Page<House> getPageFromBatch(int pageNo,
                                        int pageSize,
                                        int visitedEntries,
                                        int batchNo,
                                        int batchSize,
                                        List<House> batch) {
        // "Reset" start after each batch
        int start = visitedEntries - (batchSize * batchNo);
        int end = start + pageSize;

        // "Reset" sub-page request (of current batch)
        int subPageNo = pageNo - (batchSize / pageSize) * batchNo;
        Pageable pageable = PageRequest.of(subPageNo - 1, pageSize);

        try {
            return new PageImpl<>(batch.subList(start, end), pageable, batch.size());
        } catch (Exception e) {
            // If not enough results to have a full page, shift end
            end = batch.size();

            // If invalid request, return empty page
            if (start > end) {
                return new PageImpl<>(Collections.emptyList(), pageable, batch.size());
            }
            // Else return all is left
            return new PageImpl<>(batch.subList(start, end), pageable, batch.size());
        }

    }

    // Get one by ID
    @GetMapping("/{houseId}")
    public House getHouseById(@PathVariable("houseId") Long houseId) {
        return houseService.getHouseById(houseId);
    }

    // Add new one
    @PostMapping(
            path = "",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public ResponseEntity<House> addNewHouse(@ModelAttribute HouseForm house, @RequestParam("files") MultipartFile[] file) {
        return new ResponseEntity<>(houseService.addNewHouse(house, file), HttpStatus.OK);
    }

    // Update one by ID
    @PutMapping("/{houseId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public House updateHouseById(@PathVariable("houseId") Long houseId, @RequestBody HouseForm house) {
        return houseService.updateHouseById(houseId, house);
    }

    // Add more images to a house by ID
    @PutMapping(
            path = "/addHouseImage",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public House addHouseImage(@RequestParam("houseId") Long houseId, @RequestParam("files") MultipartFile[] file) {
        return houseService.addMoreImagesToHouse(houseId, file);
    }

    // Delete one by ID
    @DeleteMapping("/{houseId}")
    @PreAuthorize("hasAuthority('read:admin-messages')")
    public ResponseEntity<String> deleteHouseById(@PathVariable("houseId") Long houseId) {
        return new ResponseEntity<>(houseService.deleteHouseById(houseId), HttpStatus.OK);
    }
}
