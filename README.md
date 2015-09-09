## api

This is a small public api service that allows the users to query against the SiBBr's occurrence data portal information.

Basic queries implemented:
- Occurrence search given a scientific name

Information returned:
- List of all occurrences that match the filters, with three fields only for every record: an internal identifier (auto_id), that leads to the occurrence page in the [explorer](https://github.com/sibbr/explorador); decimallatitude (latitude), and decimal longitude (decimallongitude); 
