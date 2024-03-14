import { Button, CircularProgress, Dialog, DialogContent, Table, TableBody, TableCell, TableHead, TableRow, Tooltip } from "@mui/material";
import axios from "axios";
import _ from "lodash";
import { useEffect, useState } from "react";
import ErrorSnackBar from "../../shared/components/snackbar/ErrorSnackBar";
import SuccessSnackBar from "../../shared/components/snackbar/SuccessSnackBar";
import DeleteModal from "../../shared/components/deleteModal/delete-modal.component";
import { URL_API } from "../../shared/env/env";
import "../../theme/form.scss";
import "../../theme/list.scss";
import { Marque } from "../../shared/types/Marque.ts";
import { Produit } from "../../shared/types/Produit.ts";
import { ProduitForm } from "./produit-form.component";


interface ProduitListeState {
  data: Produit[];
  loading: boolean;
  error: string | null;
  success: string | null;
  openForm: boolean;
  id: string | undefined;
  openDelete: boolean;
  laodingDelete: boolean;
}

const initialState : ProduitListeState = {
  data: [],
  loading: false,
  error: null,
  success: null,
  openForm: false,
  id: undefined,
  openDelete: false,
  laodingDelete: false,
}

const ProduitListe = () => {
  document.title = " produit";
  const [state, setState] = useState<ProduitListeState>(initialState);
  useEffect(() => {
    setState((state) => ({
      ...state,
      loading: true,
      error: null,
      success: null,
    }));
    axios
      .get(`${URL_API}/produit.do`)
      .then((res) => {
        console.log(res.data);

        setState((state) => ({
          ...state,
          data: res.data.data,
          loading: false,
        }));
      })
      .catch((err) => {
        let errorMessage = "Une erreur s'est produite";
        if (
          err.response?.data.err &&
          err.response?.data.err != "" &&
          err.response?.data.err != null
        ) {
          errorMessage = err.response.data.err;
        }
        setState((state) => ({
          ...state,
          error: errorMessage,
          loading: false,
        }));
      });
  }, []);


  
  const openForm = (id?: string) => {
    setState((state) => ({
      ...state,
      openForm: true,
      id: id,
    }));
  };

  const openDeleteForm = (id?: string) => {
    setState((state) => ({
      ...state,
      id: id,
      openDelete: true,
    }));
  };

   const onDelete = (id: string) => {
    console.log("DELETE");

    setState((state) => ({
      ...state,
      loadingDelete: true,
    }));
    const options = {
      method: "DELETE",
      url: `${URL_API}/deleteproduit.do?idproduit=${id}`
    };
    axios(options)
      .then((res) => {
        console.log(res);

        setState((state) => ({
          ...state,
          success: "Suppression réussie",
          loadingDelete: false,
          openDelete: false,
        }));
        _.remove(state.data, (value) => value?.idproduit === Number(state.id));
      })
      .catch((err) => {
        console.error(err);

        let errorMessage = "Une erreur s'est produite";
        if (
          err.response?.data.err &&
          err.response?.data.err != "" &&
          err.response?.data.err != null  
        ) {
          errorMessage = err.response.data.err;
        }
        setState((state) => ({
          ...state,
          error: errorMessage,
          loadingDelete: false,
          openDelete: false,
        }));
      });
  };

    return (
    <>
      <div>
        <div className="crud-list-container">
          <div className="list-actions inline-flex-end">
            <Button variant="contained" onClick={() => openForm()} className="btn">
              Ajouter
            </Button>
          </div>
          {state.loading ? (
            <CircularProgress className="text-center" />
          ) : (
            <>

            <div className="list-tab">
            <Tale>
              <TableHead>
                
                <TableCell> Nom </TableCell>
                <TableCell> Prix </TableCell>
                <TableCell> Marque </TableCell>
                <TableCell> Disponibilite </TableCell>
                <TableCell colSpan={2} className="text-center">Actions</TableCell>
              </TableHead>
              <TableBody>
                {state.data.map((d, index) => (
                  <TableRow
                    key={`d_${index}`}
                    className={
                      state.id
                        ? Number(state.id) === d.idproduit
                          ? "selected"
                          : ""
                        : ""
                    }
                  >
                    
                  <TableCell>{d.nom}</TableCell>
                  <TableCell>{d.prix}</TableCell>
                  <TableCell>{d.marque.label}</TableCell>
                  <TableCell>{d.disponibilite}</TableCell>
                    <Tooltip title={"Modifier"}>
                      <Button
                        onClick={() =>
                          openForm((d.idproduit as number).toString())
                        }
                      >
                        <EditIcon />
                      </Button>
                    </Tooltip>
                    <Tooltip title={"Supprimer"}>
                      <Button
                        className="danger"
                        onClick={() =>
                          openDeleteForm(
                            (d.idproduit as number).toString()
                          )
                        }
                      >
                        <DeleteForeverIcon />
                      </Button>
                    </Tooltip>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
            </div>
                   {/* AJOUT/UPDATE MODAL */}
              <Dialog open={state.openForm} className="form-dialog">
                <DialogContent>
                  <ProduitForm id={state.idproduit} />
                  <div className="vertical-margin">
                    <Button
                      variant="contained"
                      onClick={() =>
                        setState((state) => ({
                          ...state,
                          openForm: false,
                        }))
                      }
                    >
                      Annuler
                    </Button>
                  </div>
                </DialogContent>
              </Dialog>
                {/* DELETE MODAL */}
              <div>
                <Dialog
                  open={state.openDelete}
                  className="form-dialog delete-dialog"
                  sx={{
                    topScrollPaper: {
                      alignItems: "flex-start",
                    },
                    topPaperScrollBody: {
                      verticalAlign: "top",
                    },
                  }}
                >
                 <DialogContent>
                    <DeleteModal
                      onClose={() =>
                        setState((state) => ({
                          ...state,
                          openDelete: false,
                        }))
                      }
                      onConfirm={() => onDelete(state.idproduit as string)}
                      loading={state.laodingDelete}
                    />
                  </DialogContent>
                </Dialog>
                  </div>
            </>
          )}
            </div>
      </div>
      <ErrorSnackBar
        open={state.error != null}
        onClose={() =>
          setState(() => ({
            ...state,
            error: null,
          }))
        }
        error={state.error as string}
      />
      <SuccessSnackBar
        open={state.success != null}
        onClose={() =>
          setState(() => ({
            ...state,
            success: null,
          }))
        }
        message={state.success as string}
      />
    </>
  );
};

export default ProduitListe;
