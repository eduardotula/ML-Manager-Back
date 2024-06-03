ALTER TABLE public.anuncio ADD avaliable_quantity INTEGER;
UPDATE anuncio set avaliable_quantity = 0;
ALTER TABLE anuncio ALTER COLUMN avaliable_quantity SET NOT NULL;
